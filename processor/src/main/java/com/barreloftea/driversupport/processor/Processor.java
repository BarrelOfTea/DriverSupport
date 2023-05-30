package com.barreloftea.driversupport.processor;

import com.barreloftea.driversupport.cameraservice.service.CameraService;
import com.barreloftea.driversupport.cameraservice.service.CameraServiceFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class Processor extends Thread{

    //TODO consider using AtomicBoolean if you will access it from multiple threads
    private AtomicBoolean exitFlag = new AtomicBoolean(false);


    public void stopAsync(){
        interrupt();
        exitFlag.set(true);
    }


    @Override
    public void run() {
//        for (int i=0;i<200;i++) {
//            if (exit) break;
//            try {
//                Log.v("aaa", "This is new message from Processor thread"+i);
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                System.out.println(Arrays.toString(e.getStackTrace()));
//            }
//        }

        CameraService cameraService = CameraServiceFactory.getCameraService();
        cameraService.start();


    }
}
