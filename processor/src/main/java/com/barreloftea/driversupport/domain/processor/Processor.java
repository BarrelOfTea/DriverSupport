package com.barreloftea.driversupport.domain.processor;

import android.util.Log;

import com.barreloftea.driversupport.domain.imageprocessor.service.ImageProcessor;
import com.barreloftea.driversupport.domain.processor.common.ImageBuffer;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;


public class Processor extends Thread {

    private AtomicBoolean exitFlag = new AtomicBoolean(false);
    private ImageProcessor imageProcessor;
    //private PulseProcessor pulseProcessor;
    //private LedController ledController;

    @Inject
    public Processor(ImageProcessor c){
        imageProcessor = c;
    }

//    @Inject
//    public Processor(LedController s){
//        ledController = s;
//    }


    public void stopAsync(){
        exitFlag.set(true);
        ImageBuffer.isProcessorRunning.set(false);
        Log.v("aaa", "processor thread is stopped");
        interrupt();
    }


    @Override
    public void run() {

        imageProcessor.start();
        //pulseProcessor.start();
        ImageBuffer.isProcessorRunning.set(true);
        Log.v("aaa", "camera service started");


        while(!exitFlag.get()){
           // Log.v("bbb", "processor thread is running");
//            ledController.sendOnCmd();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            ledController.sendOffCmd();
        }

        ImageBuffer.isProcessorRunning.set(false);
        if (imageProcessor!=null) imageProcessor.stopAsync();
        //if (pulseProcessor !=null) pulseProcessor.stopAsync();
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

//ImageProcessor cameraService = ImageProcessorFactory.getCameraService();