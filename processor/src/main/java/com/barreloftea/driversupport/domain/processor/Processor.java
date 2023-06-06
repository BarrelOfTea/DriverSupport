package com.barreloftea.driversupport.domain.processor;

import android.util.Log;

import com.barreloftea.driversupport.domain.processor.common.ImageBuffer;
import com.barreloftea.driversupport.domain.imageprocessor.service.CameraService;

import java.util.concurrent.atomic.AtomicBoolean;


public class Processor extends Thread {

    private AtomicBoolean exitFlag = new AtomicBoolean(false);
    private CameraService cameraService;

    //@Inject
    public Processor(CameraService c){
        cameraService = c;
    }


    public void stopAsync(){
        exitFlag.set(true);
        ImageBuffer.isProcessorRunning.set(false);
        Log.v("aaa", "processor thread is stopped");
        interrupt();
    }


    @Override
    public void run() {

        cameraService.start();
        ImageBuffer.isProcessorRunning.set(true);
        Log.v("aaa", "camera service started");

        while(!exitFlag.get()){
           // Log.v("bbb", "processor thread is running");
        }

        ImageBuffer.isProcessorRunning.set(false);
        if (cameraService!=null) cameraService.stopAsync();
    }


}



//        for (int i=0;i<200;i++) {
//            if (exitFlag.get()) break;
//            try {
//                Log.v("aaa", "This is new message from Processor thread"+i);
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                System.out.println(Arrays.toString(e.getStackTrace()));
//            }
//        }

//CameraService cameraService = CameraServiceFactory.getCameraService();