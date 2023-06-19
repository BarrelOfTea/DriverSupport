package com.barreloftea.driversupport.domain.imageprocessor.service;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import com.barreloftea.driversupport.domain.imageprocessor.interfaces.VideoRepository;
import com.barreloftea.driversupport.domain.imageprocessor.utils.DrawContours;
import com.barreloftea.driversupport.domain.processor.Processor;
import com.barreloftea.driversupport.domain.processor.common.Constants;
import com.barreloftea.driversupport.domain.processor.common.ImageBuffer;
import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ImageProcessor extends Thread {

    public static final String TAG = ImageProcessor.class.getSimpleName();

    private AtomicBoolean exitFlag = new AtomicBoolean(false);
    VideoRepository videoRepository;
    SharedPrefRepository sharedPrefRepository;
    ArrayBlockingQueue<Bitmap> queue;
    ImageBuffer imageBuffer;
    Processor processor;

    volatile boolean isBusy = false;

    //these thresholds will be measured in milliseconds
    static final int CLOSED_THRESH = 2500;
    static final int MOUTH_OPEN_THRESH = 4000;
    static final int OPEN_THRESH = 12000;
    static final float ROUND = 0.9f;

    public float EOP;
    public float MOR;
    public int EULER_X;
    public int EULER_Z;

    private float lastEOP;
    private float lastMOR;
    long closedEyesTime;
    long mouthOpenTime;
    long notBlinkTime;

    private DrawContours drawer = new DrawContours();

    private Bitmap bitmap;
    InputImage inputImage;

    public ImageProcessor(VideoRepository rep, SharedPrefRepository prefRepository){
        videoRepository = rep;
        sharedPrefRepository = prefRepository;
    }

    public void init(String rtsp, String username, String password, Processor processor) {
        this.processor = processor;
        videoRepository.setParams(rtsp, username, password);
        videoRepository.prepare();
        imageBuffer = ImageBuffer.getInstance();

        EOP = sharedPrefRepository.getEOP();
        MOR = sharedPrefRepository.getMOR();
        EULER_X = sharedPrefRepository.getEulerX();
        EULER_Z = sharedPrefRepository.getEulerZ();
    }

    private FaceDetectorOptions realTimeOpts = new FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            //.setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build();
    private FaceDetector detector = FaceDetection.getClient(realTimeOpts);

    public void stopAsync(){
        exitFlag.set(true);
        videoRepository.stopAsync();
        interrupt();
        Log.v(TAG, "camara thread is stopped");
    }


    @Override
    public void run() {
        queue = videoRepository.getVideoQueue();
        Log.v(TAG, "camera thread started");

        AtomicLong closedStartTime = new AtomicLong(System.currentTimeMillis());
        AtomicLong openStartTime = new AtomicLong(System.currentTimeMillis());

        while(!exitFlag.get()){

            try {
                bitmap = queue.take();
                Log.v(TAG, "image is taken from queue");
            } catch (InterruptedException e) {
                Log.v(TAG, "no bitmap available in queue");
            }

            long startTime = System.nanoTime();

            inputImage = InputImage.fromBitmap(bitmap, 0);

            if (!isBusy) {
                isBusy = true;
                //Task<List<Face>> result =
                detector.process(inputImage)
                        .addOnSuccessListener(
                                faces -> {

                                    Log.v(TAG, faces.size() + " FACES WERE DETECTED");

                                    for (Face face : faces) {
                                        Rect bounds = face.getBoundingBox();
                                        bitmap = drawer.drawRect(bitmap, bounds);

                                        float rotY = face.getHeadEulerAngleY();  // To the right of the camera
                                        float rotZ = face.getHeadEulerAngleZ();  // Counter-clockwise to the camera
                                        float rotX = face.getHeadEulerAngleX();  // Upward

                                        if (face.getContour(FaceContour.LEFT_EYE) != null) {
                                            List<PointF> leftEyeContour = face.getContour(FaceContour.LEFT_EYE).getPoints();
                                            //bitmap = drawer.drawContours(bitmap, leftEyeContour);
                                            List<PointF> rightEyeContour = Objects.requireNonNull(face.getContour(FaceContour.RIGHT_EYE)).getPoints();
                                            bitmap = drawer.drawContours(bitmap, rightEyeContour);
                                            List<PointF> upperLipCon = Objects.requireNonNull(face.getContour(FaceContour.UPPER_LIP_TOP)).getPoints();
                                            bitmap = drawer.drawContours(bitmap, upperLipCon);
                                            List<PointF> lowerLipCon = Objects.requireNonNull(face.getContour(FaceContour.LOWER_LIP_BOTTOM)).getPoints();
                                            bitmap = drawer.drawContours(bitmap, lowerLipCon);
                                            List<PointF> noseCon = Objects.requireNonNull(face.getContour(FaceContour.NOSE_BRIDGE)).getPoints();
                                            bitmap = drawer.drawContours(bitmap, noseCon);


                                            float REOP = getOneEOP(rightEyeContour);
                                            float LEOP = getOneEOP(leftEyeContour);


                                            notBlinkTime++;

                                            lastEOP = (LEOP + REOP) / 2;

                                            Log.v(TAG, "last eop is" + lastEOP);

                                            if (lastEOP < EOP) {
                                                closedEyesTime = System.currentTimeMillis() - closedStartTime.get();
                                                ;
                                                notBlinkTime = 0;
                                                openStartTime.set(System.currentTimeMillis());
                                                Log.v(null, "you blinked");
                                            } else {
                                                closedStartTime.set(System.currentTimeMillis());
                                                closedEyesTime = 0;
                                                closedEyesTime = System.currentTimeMillis() - closedStartTime.get();
                                            }

                                            if (closedEyesTime >= CLOSED_THRESH) {
                                                processor.setCamState(Constants.SLEEPING);
                                                Log.v(null, "REASON closed eyes");
                                            }
                                            if (notBlinkTime > OPEN_THRESH) {
                                                processor.setCamState(Constants.SLEEPING);
                                                Log.v(null, "REASON always open eyes");
                                            }

                                            float mor = getMOR(upperLipCon, lowerLipCon);
                                            lastMOR = mor;

                                        /*if (mor > MOR) mouthFlag++;
                                        else {
                                            mouthFlag = 0;
                                        }
                                        Log.v(null, "mouthflag is "+mouthFlag+" with mor "+MOR);
                                        if (mouthFlag>=MOUTH_THRESH){
                                            processor.setCamState(Processor.DROWSY);
                                            Log.v(null, "REASON yawn");
                                        }*/

                                            if (closedEyesTime < CLOSED_THRESH && notBlinkTime < OPEN_THRESH/*&& mouthFlag<MOUTH_THRESH && noseFlag<EYE_THRESH*/) {
                                                Log.v(null, "awake again");
                                                processor.setCamState(Constants.AWAKE);

                                            }

                                            Log.v(null, rotY + " roty");
                                            Log.v(null, rotZ + " rotz");
                                            Log.v(null, rotX + " rotx");
                                        }
                                    }
                                })
                        .addOnFailureListener(
                                e -> Log.v(TAG, "IMAGE PROCESSING FAILED" + Arrays.toString(e.getStackTrace()) + e.getMessage()))
                        .addOnCompleteListener(
                                task -> {
                                    Log.v(TAG, "IMAGE IS PROCESSED SUCCESSFULLY");
                                    long endTime = System.nanoTime();
                                    long timePassed = endTime - startTime;
                                    Log.v(null, "Execution time in milliseconds: " + timePassed / 1000000);
                                    isBusy=false;

                                    imageBuffer.setFrame(bitmap);
                                    Log.v(TAG, "image onFrame called");
                                }
                        );
                inputImage = null;
            }
        }

        detector.close();
        drawer = null;
        bitmap.recycle();
        inputImage = null;
        videoRepository = null;
    }


    //you can do it, move on!

    float getMOR(List<PointF> ul, List<PointF> ll){
        PointF [] upoints = new PointF[ul.size()];
        ul.toArray(upoints);

        PointF [] lpoints = new PointF[ll.size()];
        ll.toArray(lpoints);

        float ver = (float) Math.sqrt(Math.pow(upoints[5].x - lpoints[4].x, 2) + Math.pow(upoints[5].y - lpoints[4].y, 2));
        float hor = (float) Math.sqrt(Math.pow(upoints[0].x - upoints[10].x, 2) + Math.pow(upoints[0].y - upoints[10].y, 2));


        return ver/hor;
    }

    //re = right eye, ru = right upper
    float getOneEOP(List<PointF> contour){

        PointF [] points = new PointF[contour.size()];
        contour.toArray(points);

        float rVer = (float) Math.sqrt(Math.pow(points[4].x - points[12].x, 2) + Math.pow(points[4].y - points[12].y, 2));
        float rHor = (float) Math.sqrt(Math.pow(points[0].x - points[8].x, 2) + Math.pow(points[0].y - points[8].y, 2));

        return rVer/rHor;
    }

    public void onEOPupdate(){
        EOP = lastEOP * ROUND;
        sharedPrefRepository.saveEOP(EOP);
        Log.v(TAG, "new eop is set to" + EOP);
        MOR = lastMOR;
        Log.v(TAG, "new mor is set to" + MOR);
        sharedPrefRepository.saveMOR(MOR);
    }

}


/*
    float getNL(List<PointF> contour){
        PointF [] points = new PointF[contour.size()];
        contour.toArray(points);

        return (float) Math.sqrt(Math.pow(points[0].x - points[1].x, 2) + Math.pow(points[0].y - points[1].y, 2));
    }
 */
