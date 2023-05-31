package com.barreloftea.driversupport.processor;

import android.util.Log;

import com.barreloftea.driversupport.cameraservice.service.CameraService;
import com.barreloftea.driversupport.cameraservice.service.CameraServiceFactory;
import com.barreloftea.driversupport.processor.common.ImageBuffer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;


public class Processor extends Thread {

    private AtomicBoolean exitFlag = new AtomicBoolean(false);
    private CameraService cameraService;

    //@Inject
    public Processor(CameraService c){
        cameraService = c;
    }


    public void stopAsync(){
        interrupt();
        exitFlag.set(true);
        ImageBuffer.isProcessorRunning.set(false);
    }


    @Override
    public void run() {
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



//
        cameraService.start();
        ImageBuffer.isProcessorRunning.set(true);
        Log.v("aaa", "camera service started");


    }
}