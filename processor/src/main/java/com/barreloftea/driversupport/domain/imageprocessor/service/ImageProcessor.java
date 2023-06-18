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
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImageProcessor extends Thread {

    public static final String TAG = ImageProcessor.class.getSimpleName();

    private AtomicBoolean exitFlag = new AtomicBoolean(false);
    VideoRepository videoRepository;
    ArrayBlockingQueue<Bitmap> queue;
    ImageBuffer imageBuffer;
    Processor processor;
    int eyeFlag;
    int mouthFlag;
    int noseFlag;
    int notBlinkFlag;
    volatile boolean isBusy = false;
    static final int EYE_THRESH = 4;
    static final int MOUTH_THRESH = 18;
    static final int NO_BLINK_TH = 80;
    static final float ROUND = 0.6f;

    public float EOP_ = 0.5f;
    public float MOR_ = 0.5f;
    public float NL_ = 0.5f;
    private float lastEOP;
    private float lastMOR;
    private float lastNL;

    private DrawContours drawer = new DrawContours();

    private Bitmap bitmap;
    InputImage inputImage;

    public ImageProcessor(VideoRepository rep){
        videoRepository = rep;
    }

    public void init(String rtsp, String username, String password, Processor processor) {
        this.processor = processor;
        videoRepository.setParams(rtsp, username, password);
        videoRepository.prepare();
        imageBuffer = ImageBuffer.getInstance();
    }

    private FaceDetectorOptions realTimeOpts = new FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
             .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            //.setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
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

        while(!exitFlag.get()){
            //ByteBuffer byteBuffer; //NOTICE you can change overload of method here too
            //Image image;
            //ImageByteData ibd = null;
            //InputImage inputImage = null;
            try {
                //byteBuffer = queue.take();
                bitmap = queue.take();
                Log.v(TAG, "image is taken from queue");
                //Log.v(TAG, String.valueOf(bitmap.getByteCount()));
            } catch (InterruptedException e) {
                //throw new RuntimeException(e);
                Log.v(TAG, "no bitmap available in queue");
            }
            /*InputImage inputImage = InputImage.fromByteBuffer(
                    byteBuffer,1280, 720, 0,
                    InputImage.IMAGE_FORMAT_NV21 // or IMAGE_FORMAT_YV12
            );
            bitmap = Bitmap.createBitmap(1280, 720, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(byteBuffer);*/


            inputImage = InputImage.fromBitmap(bitmap, 0);
            //InputImage inputImage = InputImage.fromByteArray(ibd.getBytes(), ibd.getWidth(), ibd.getHeight(), ibd.getRotationDegrees(), ibd.getFormat());

            //bitmap = inputImage.getBitmapInternal();
            long startTime = System.nanoTime();

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

                                        List<PointF> leftEyeContour = face.getContour(FaceContour.LEFT_EYE).getPoints();
                                        //bitmap = drawer.drawContours(bitmap, leftEyeContour);
                                        List<PointF> rightEyeContour = face.getContour(FaceContour.RIGHT_EYE).getPoints();
                                        bitmap = drawer.drawContours(bitmap, rightEyeContour);
                                        List<PointF> upperLipCon = face.getContour(FaceContour.UPPER_LIP_TOP).getPoints();
                                        bitmap = drawer.drawContours(bitmap, upperLipCon);
                                        List<PointF> lowerLipCon = face.getContour(FaceContour.LOWER_LIP_BOTTOM).getPoints();
                                        bitmap = drawer.drawContours(bitmap, lowerLipCon);
                                        List<PointF> noseCon = face.getContour(FaceContour.NOSE_BRIDGE).getPoints();
                                        bitmap = drawer.drawContours(bitmap, noseCon);


                                        float REOP = getOneEOP(rightEyeContour);
                                        float LEOP = getOneEOP(leftEyeContour);


                                        notBlinkFlag++;

                                        lastEOP = (LEOP + REOP) / 2;

                                        Log.v(TAG, "last eop is" + lastEOP);

                                        if ((LEOP + REOP) / 2 < EOP_) {
                                            eyeFlag++;
                                            notBlinkFlag = 0;
                                            Log.v(null, "you blinked");
                                        } else {
                                            eyeFlag = 0;
                                        }

                                        if (eyeFlag >= EYE_THRESH) {
                                            processor.setCamState(Constants.SLEEPING);
                                            Log.v(null, "REASON closed eyes");
                                        }
                                        if (notBlinkFlag > NO_BLINK_TH) {
                                            //processor.setCamState(Processor.DROWSY);
                                            Log.v(null, "REASON always open eyes");
                                        }


                                            /*float MOR = getMOR(upperLipCon, lowerLipCon);
                                            lastMOR = MOR;

                                            if (MOR > MOR_) mouthFlag++;
                                            else {
                                                mouthFlag = 0;
                                            }
                                            Log.v(null, "mouthflag is "+mouthFlag+" with mor "+MOR);
                                            if (mouthFlag>=MOUTH_THRESH){
                                                processor.setCamState(Processor.DROWSY);
                                                Log.v(null, "REASON yawn");
                                            }*/

                                        if (eyeFlag < EYE_THRESH /*&& mouthFlag<MOUTH_THRESH && noseFlag<EYE_THRESH*/) {

//                                                new Thread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
                                            Log.v(null, "awake again");
                                            processor.setCamState(Constants.AWAKE);
//                                                    }
//                                                }).start();
                                        }

                                            /*float nl = getNL(noseCon);
                                            lastNL = nl;

                                            if (nl < NL_) noseFlag++;
                                            else {
                                                noseFlag = 0;
                                            }
                                            Log.v(null, "nose flag is "+noseFlag+" with nose length "+nl);
                                            if (noseFlag >= EYE_THRESH){
                                                processor.setCamState(Processor.SLEEPING);
                                                Log.v(null, "REASON dosed off");
                                            }*/

                                        //log(LEOP, REOP, MOR, rotY, rotZ, nl);

                                        Log.v(null, rotY + " roty");
                                        Log.v(null, rotZ + " rotz");
                                        Log.v(null, rotX + " rotx");
                                    }
                                })
                        .addOnFailureListener(
                                e -> Log.v(TAG, "IMAGE PROCESSING FAILED" + Arrays.toString(e.getStackTrace()) + e.getMessage()))
                        .addOnCompleteListener(
                                task -> {
                                    //imageBuffer.imageQueue.offer(bitmap);
                                    Log.v(TAG, "IMGAE IS PROCESSED SUCCESSFULLY");
                                    //image.close();
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

        /*
        PointF upper = new PointF(0,0);
        PointF lower= new PointF(0,0);
        //these are points for calculating width of a mouth
        PointF leftCorner = new PointF(0,0);
        PointF rightCorner = new PointF(0,0);

        boolean mouthOpen = false;

        for (int i = 0; i < 8; i++){
            if (i==0){
                leftCorner = ul.iterator().next();
            }
            if (i==4) {
                upper = ul.iterator().next();
                lower = ll.iterator().next();
            }
            if (i==8){
                rightCorner = ul.iterator().next();
            }
        }

        float mouthDistVert = (float) Math.sqrt((upper.x - lower.x) * (upper.x - lower.x) + (upper.y - lower.y) * (upper.y - lower.y));
        float mouthDistHor = (float) Math.sqrt(Math.pow(leftCorner.x - rightCorner.x, 2) + Math.pow(leftCorner.y - rightCorner.y, 2));
        float MOR = mouthDistVert/mouthDistHor;

        Log.v(null, "calculated MOR is "+ MOR);
        return MOR;
           */
    }

    //re = right eye, ru = right upper
    float getOneEOP(List<PointF> contour){

        PointF [] points = new PointF[contour.size()];
        contour.toArray(points);

        float rVer = (float) Math.sqrt(Math.pow(points[4].x - points[12].x, 2) + Math.pow(points[4].y - points[12].y, 2));
        float rHor = (float) Math.sqrt(Math.pow(points[0].x - points[8].x, 2) + Math.pow(points[0].y - points[8].y, 2));


        return rVer/rHor;

    }

    float getNL(List<PointF> contour){
        PointF [] points = new PointF[contour.size()];
        contour.toArray(points);

        float nl = (float) Math.sqrt(Math.pow(points[0].x - points[1].x, 2) + Math.pow(points[0].y - points[1].y, 2));

        return nl;
    }

    public void onEOPupdate(){
        EOP_ = lastEOP;
        Log.v(TAG, "new eop is set to" + EOP_);
        MOR_ = lastMOR;
        Log.v(TAG, "new eop is set to" + MOR_);
        NL_ = lastNL;
        Log.v(TAG, "new eop is set to" + NL_);
    }

}
