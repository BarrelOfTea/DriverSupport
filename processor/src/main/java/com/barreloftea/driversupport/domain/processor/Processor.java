package com.barreloftea.driversupport.domain.processor;

import android.content.Context;
import android.util.Log;

import com.barreloftea.driversupport.domain.imageprocessor.service.ImageProcessor;
import com.barreloftea.driversupport.domain.processor.common.ImageBuffer;
import com.barreloftea.driversupport.domain.pulseprocessor.service.PulseProcessor;
import com.barreloftea.driversupport.domain.soundcontroller.SoundController;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;


public class Processor extends Thread {

    public static final int AWAKE = 0;
    public static final int DROWSY = 1;
    public static final int SLEEPING = 2;


    private AtomicBoolean exitFlag = new AtomicBoolean(false);
    private int stateCam = 0;
    private int stateBand = 0;
    public Context context;

    @Inject
    ImageProcessor imageProcessor;

    @Inject
    PulseProcessor pulseProcessor;
    @Inject
    SoundController soundController;
    //private PulseProcessor pulseProcessor;
    //private LedController ledController;

    @Inject
    public Processor(ImageProcessor i, PulseProcessor p, SoundController s){
        imageProcessor = i;
        imageProcessor.setProcessor(this);

        pulseProcessor = p;
        pulseProcessor.setProcessor(this);

        soundController = s;
    }

    public void init(Context context){
        soundController.init(context);
        this.context = context;
    }




    public void stopAsync(){
        exitFlag.set(true);
        ImageBuffer.isProcessorRunning.set(false);
        Log.v("aaa", "processor thread is stopped");
        interrupt();
    }


    @Override
    public void run() {

        imageProcessor.start();

        pulseProcessor.prepare("D7:71:B3:98:F8:57");
        pulseProcessor.start();
        ImageBuffer.isProcessorRunning.set(true);
        Log.v("aaa", "camera service started");


        while(!exitFlag.get()){
            if (stateCam == AWAKE){
                soundController.pause();
            }
            if (stateCam == SLEEPING) {
                soundController.play();
            }
        }

        ImageBuffer.isProcessorRunning.set(false);
        if (imageProcessor!=null) imageProcessor.stopAsync();
        if (soundController!=null) soundController.destroy();
        if (pulseProcessor !=null) pulseProcessor.stopAsync();
    }

    public synchronized void setCamState(int s){
        this.stateCam = s;
    }

    public synchronized void setBandState(int s){
        this.stateBand = s;
    }


}


//    @Inject
//    public Processor(LedController s){
//        ledController = s;
//    }


//            Log.v("bbb", "processor thread is running");
//            ledController.sendOnCmd();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            ledController.sendOffCmd();





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