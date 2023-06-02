package com.barreloftea.driversupport.cameraservice.service;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import com.barreloftea.driversupport.cameraservice.interfaces.VideoRepository;
import com.barreloftea.driversupport.cameraservice.utils.DrawContours;
import com.barreloftea.driversupport.processor.common.ImageBuffer;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class CameraService extends Thread {

    private AtomicBoolean exitFlag = new AtomicBoolean(false);
    VideoRepository videoRepository;
    ArrayBlockingQueue<ByteBuffer> queue;

    public CameraService(VideoRepository rep){
        videoRepository = rep;
        queue = rep.getVideoQueue();
    }


    int eyeFlag;
    int mouthFlag;
    int noseFlag;
    int notBlinkFlag;
    static final int EYE_THRESH = 16;
    static final int MOUTH_THRESH = 18;
    static final int NO_BLINK_TH = 80;
    static final float ROUND = 0.6f;

    private DrawContours drawer = new DrawContours();

    private Bitmap bitmap;
    private FaceDetectorOptions realTimeOpts = new FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build();
    private FaceDetector detector = FaceDetection.getClient(realTimeOpts);

    public void stopAsync(){
        exitFlag.set(true);
        interrupt();
        Log.v("aaa", "camara thread is stopped");
    }


    @Override
    public void run() {
        while(!exitFlag.get()){
            ByteBuffer byteBuffer; //NOTICE you can change overload of method here too
            try {
                byteBuffer = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            long startTime = System.nanoTime();
            InputImage inputImage = InputImage.fromByteBuffer(
                    byteBuffer,1280, 720, 0,
                    InputImage.IMAGE_FORMAT_NV21 // or IMAGE_FORMAT_YV12
            );
            bitmap = Bitmap.createBitmap(1280, 720, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(byteBuffer);

            Task<List<Face>> result =
                    detector.process(inputImage)
                            .addOnSuccessListener(
                                    faces -> {
                                        Log.v(null, faces.size() + " FACES WERE DETECTED");

                                        for (Face face : faces){
                                            Rect bounds = face.getBoundingBox();
                                            bitmap = drawer.drawRect(bitmap, bounds);


                                            float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                                            float rotZ = face.getHeadEulerAngleZ(); //TODO rotY and rotZ are somehow always 0.0 and -0.0
                                            float rotX = face.getHeadEulerAngleX();

                                            List<PointF> leftEyeContour = face.getContour(FaceContour.LEFT_EYE).getPoints();
                                            bitmap = drawer.drawContours(bitmap, leftEyeContour);
                                            List<PointF> rightEyeContour = face.getContour(FaceContour.RIGHT_EYE).getPoints();
                                            bitmap = drawer.drawContours(bitmap, rightEyeContour);
                                            List<PointF> upperLipCon = face.getContour(FaceContour.UPPER_LIP_TOP).getPoints();
                                            bitmap = drawer.drawContours(bitmap, upperLipCon);
                                            List<PointF> lowerLipCon = face.getContour(FaceContour.LOWER_LIP_BOTTOM).getPoints();
                                            bitmap = drawer.drawContours(bitmap, lowerLipCon);
                                            List<PointF> noseCon = face.getContour(FaceContour.NOSE_BRIDGE).getPoints();
                                            bitmap = drawer.drawContours(bitmap, noseCon);


                                        /*long endTime = System.nanoTime();
                                        long timePassed = endTime - startTime;
                                        Log.v(null, "Execution time before eop " + timePassed / 1000000);*/

                                            float REOP = getOneEOP(rightEyeContour);
                                            float LEOP = getOneEOP(leftEyeContour);

                                        /*long endTime1 = System.nanoTime();
                                        long timePassed1 = endTime1 - startTime;
                                        Log.v(null, "Execution time after eop: " + timePassed1 / 1000000);*/

                                            notBlinkFlag++;

//                                            if ((LEOP+REOP)/2 < activity.params.getEOP()) {
//                                                eyeFlag++;
//                                                notBlinkFlag = 0;
//                                                Log.v(null, "you blinked");
//                                            }
//                                            else {
//                                                eyeFlag = 0;
//                                            }
//
//                                            if (eyeFlag>=EYE_THRESH){
//                                                activity.enableAlert("WAKE UP! FIND A SPOT TO HAVE REST");
//                                                Log.v(null, "REASON closed eyes");
//                                            }
//                                            if (notBlinkFlag > NO_BLINK_TH){
//                                                activity.enableAlert("WAKE UP! YOU ARE SLEEPING WITH OPEN EYES");
//                                                Log.v(null, "REASON always open eyes");
//                                            }

                                        /*long endTime2 = System.nanoTime();
                                        long timePassed2 = endTime2 - startTime;
                                        Log.v(null, "Execution time before mor: " + timePassed2 / 1000000);*/
                                            float MOR = getMOR(upperLipCon, lowerLipCon);
                                        /*long endTime3 = System.nanoTime();
                                        long timePassed3 = endTime3 - startTime;
                                        Log.v(null, "Execution time after mor: " + timePassed3 / 1000000);*/
//                                            if (MOR > activity.params.getMOR()) mouthFlag++;
//                                            else {
//                                                mouthFlag = 0;
//                                            }
//                                            Log.v(null, "mouthflag is "+mouthFlag+" with mor "+MOR);
//                                            if (mouthFlag>=MOUTH_THRESH){
//                                                activity.enableWarning("YOU ARE SLEEPY! DRIVE TO THE CLOSEST PARKING TO HAVE SOME REST");
//                                                Log.v(null, "REASON yawn");
//                                            }
//
//                                            if(eyeFlag<EYE_THRESH && mouthFlag<MOUTH_THRESH && noseFlag<EYE_THRESH) activity.resetText();
//
//                                            float nl = getNL(noseCon);
//                                            if (nl < activity.params.getNL()) noseFlag++;
//                                            else {
//                                                noseFlag = 0;
//                                            }
//                                            Log.v(null, "nose flag is "+noseFlag+" with nose length "+nl);
//                                            if (noseFlag >= EYE_THRESH){
//                                                activity.enableAlert("YOU ARE DOZING OFF! DRIVE TO THE CLOSEST PARKING LOT");
//                                                Log.v(null, "REASON dosed off");
//                                            }

                                            //log(LEOP, REOP, MOR, rotY, rotZ, nl);

                                            Log.v(null, rotY + " roty");
                                            Log.v(null, rotZ + " rotz");
                                            Log.v(null, rotX + " rotx");
                                        }

                                    })
                            .addOnFailureListener(
                                    e -> System.out.println("there was an error processing an image"))
                            .addOnCompleteListener(
                                    task -> {
                                        //activity.preview.setRotation(image.getImageInfo().getRotationDegrees());
                                        //activity.setPreview(bitmap);

                                        ImageBuffer.imageQueue.offer(bitmap);
                                        Log.v("aaa", "IMGAE IS PROCESSED SUCCESSFULLY");
                                        //image.close();

                                        long endTime = System.nanoTime();
                                        long timePassed = endTime - startTime;
                                        Log.v(null, "Execution time in milliseconds: " + timePassed / 1000000);

                                    }
                            );

        }
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

}
