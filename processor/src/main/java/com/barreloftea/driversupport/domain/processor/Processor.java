package com.barreloftea.driversupport.domain.processor;

import android.content.Context;
import android.util.Log;

import com.barreloftea.driversupport.domain.imageprocessor.service.ImageProcessor;
import com.barreloftea.driversupport.domain.processor.common.Constants;
import com.barreloftea.driversupport.domain.processor.common.ImageBuffer;
import com.barreloftea.driversupport.domain.pulseprocessor.service.PulseProcessor;
import com.barreloftea.driversupport.domain.soundcontroller.SoundController;
import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;


public class Processor extends Thread {

    public static final String TAG = Processor.class.getSimpleName();

    private AtomicBoolean exitFlag = new AtomicBoolean(false);
    private volatile int stateCam = 0;
    private int stateBand = 0;
    public Context context;

    @Inject
    public ImageProcessor imageProcessor;

    @Inject
    PulseProcessor pulseProcessor;
    @Inject
    volatile SoundController soundController;

    @Inject
    SharedPrefRepository sharedPrefRepository;
    //private LedController ledController;

    @Inject
    public Processor(ImageProcessor i, PulseProcessor p, SoundController s, SharedPrefRepository rep){
        imageProcessor = i;
        imageProcessor.setName("image processor");

        pulseProcessor = p;
        pulseProcessor.setName("pulse processor");

        soundController = s;

        sharedPrefRepository = rep;
    }

    public void init(Context context){
        soundController.init(context);
        this.context = context;

        //if ()
    }

    public void stopAsync(){
        exitFlag.set(true);
        //ImageBuffer.isProcessorRunning.set(false);
        Log.v("aaa", "processor thread is stopped");
        interrupt();
    }


    @Override
    public void run() {
        Log.v(TAG, "processor thread started");

        //imageProcessor.init(this);
        //imageProcessor.start();

        pulseProcessor.init("D7:71:B3:98:F8:57", this);
        pulseProcessor.start();
        //ImageBuffer.isProcessorRunning.set(true);

        while(!exitFlag.get()){
            if (stateCam == Constants.AWAKE){
                soundController.pause();
            }
            if (stateCam == Constants.SLEEPING) {
                soundController.play();
            }
        }

        //ImageBuffer.isProcessorRunning.set(false);
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

    boolean checkBandRequired(){
        return sharedPrefRepository.getSavedBlueDevice(Constants.TYPE_BAND).isSaved();
    }

    boolean checkLedRequired(){
        boolean connected = sharedPrefRepository.getSavedBlueDevice(Constants.TYPE_LED).isSaved();
        boolean required = sharedPrefRepository.getAreSignalsOn().get(Constants.IS_LED_SIGNAL_ON);

        return (connected && required);
    }

    boolean checkSoundRequired(){
        return sharedPrefRepository.getAreSignalsOn().get(Constants.IS_SOUND_SIGNAL_ON);
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