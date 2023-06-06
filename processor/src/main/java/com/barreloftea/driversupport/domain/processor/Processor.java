package com.barreloftea.driversupport.domain.processor;

import android.util.Log;

import com.barreloftea.driversupport.domain.ledcontroller.service.LedController;
import com.barreloftea.driversupport.domain.processor.common.ImageBuffer;
import com.barreloftea.driversupport.domain.pulseprocessor.service.PulseProcessor;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;


public class Processor extends Thread {

    private AtomicBoolean exitFlag = new AtomicBoolean(false);
    //private ImageProcessor cameraService;
    //private PulseProcessor pulseProcessor;
    private LedController ledController;

    //@Inject
//    public Processor(ImageProcessor c){
//        cameraService = c;
//    }

    @Inject
    public Processor(LedController s){
        ledController = s;
    }


    public void stopAsync(){
        exitFlag.set(true);
        ImageBuffer.isProcessorRunning.set(false);
        Log.v("aaa", "processor thread is stopped");
        interrupt();
    }


    @Override
    public void run() {

        //cameraService.start();
        //pulseProcessor.start();
        ImageBuffer.isProcessorRunning.set(true);
        Log.v("aaa", "camera service started");


        while(!exitFlag.get()){
           // Log.v("bbb", "processor thread is running");
            ledController.sendOnCmd();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ledController.sendOffCmd();
        }

        ImageBuffer.isProcessorRunning.set(false);
        //if (cameraService!=null) cameraService.stopAsync();
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