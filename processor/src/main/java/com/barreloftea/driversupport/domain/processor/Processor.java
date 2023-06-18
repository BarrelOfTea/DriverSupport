package com.barreloftea.driversupport.domain.processor;


import android.content.Context;
import android.util.Log;

import com.barreloftea.driversupport.domain.R;
import com.barreloftea.driversupport.domain.imageprocessor.service.ImageProcessor;
import com.barreloftea.driversupport.domain.ledcontroller.service.LedController;
import com.barreloftea.driversupport.domain.processor.common.Constants;
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

    private boolean isBandRequired;
    private boolean isLedRequired;
    private boolean isSoundRequired;

    @Inject
    public ImageProcessor imageProcessor;

    @Inject
    PulseProcessor pulseProcessor;
    @Inject
    volatile SoundController alertSoundController;

    @Inject
    volatile SoundController warningSoundController;

    @Inject
    SharedPrefRepository sharedPrefRepository;

    @Inject
    LedController ledController;

    @Inject
    public Processor(ImageProcessor i, PulseProcessor p, SoundController sa, SoundController sw, LedController controller, SharedPrefRepository rep){
        imageProcessor = i;
        imageProcessor.setName("image processor");

        pulseProcessor = p;
        pulseProcessor.setName("pulse processor");

        alertSoundController = sa;
        warningSoundController = sw;

        ledController = controller;

        sharedPrefRepository = rep;
    }

    public void init(Context context){
        this.context = context;

        imageProcessor.init("rtsp://192.168.0.1:554/livestream/12", "", "", this);
        warningSoundController.init(context, R.raw.notification);

        isBandRequired = checkBandRequired();
        isLedRequired = checkLedRequired();
        isSoundRequired = checkSoundRequired();
    }

    public void stopAsync(){
        exitFlag.set(true);
        Log.v("aaa", "processor thread is stopped");
        interrupt();
    }


    @Override
    public void run() {
        Log.v(TAG, "processor thread started");

        imageProcessor.start();

        startPulseProcessor();

        while(!exitFlag.get()){
            if (stateCam == Constants.AWAKE && stateBand == Constants.AWAKE){
                disableSignals();
            } else if (stateCam == Constants.SLEEPING) {
                enableAlertSignals();
            } else {
                enableWarningSignal();
            }
        }

        if (imageProcessor!=null) imageProcessor.stopAsync();
        if (alertSoundController !=null) alertSoundController.destroy();
        if (warningSoundController !=null) warningSoundController.destroy();
        if (pulseProcessor !=null) pulseProcessor.stopAsync();

        alertSoundController = null;
        warningSoundController = null;
    }

    public synchronized void setCamState(int s){
        this.stateCam = s;
    }

    public synchronized void setBandState(int s){
        this.stateBand = s;
    }

    boolean checkBandRequired(){
        boolean available = sharedPrefRepository.getSavedBlueDevice(Constants.TYPE_BAND).isSaved();
        if (available) pulseProcessor.init("D7:71:B3:98:F8:57", this);
        return available;
    }

    boolean checkLedRequired(){
        boolean connected = sharedPrefRepository.getSavedBlueDevice(Constants.TYPE_LED).isSaved();
        boolean required = sharedPrefRepository.getIsSignalOn(Constants.IS_LED_SIGNAL_ON);
        boolean available = (connected && required);
        if (available) ledController.init();
        return available;
    }

    boolean checkSoundRequired(){
        boolean available = sharedPrefRepository.getIsSignalOn(Constants.IS_SOUND_SIGNAL_ON);
        if (available) alertSoundController.init(context);
        return available;
    }

    private void startPulseProcessor(){
        if (isBandRequired) pulseProcessor.start();
    }

    private void enableAlertSignals(){
        if (isSoundRequired) alertSoundController.play();
        if (isLedRequired) ledController.sendOnCmd();
    }

    private void disableSignals(){
        if (isSoundRequired) alertSoundController.pause();
        if (isLedRequired) ledController.sendOffCmd();
        warningSoundController.pause();
    }

    private void enableWarningSignal(){
        warningSoundController.play();
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