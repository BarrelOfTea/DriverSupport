package com.barreloftea.driversupport.domain.pulseprocessor.service;

import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

public class PulseProcessor extends Thread{

    private AtomicBoolean exitFlag = new AtomicBoolean(false);

    public PulseProcessor(){

    }

    public void stopAsync(){
        exitFlag.set(true);
        interrupt();
        Log.v("aaa", "pulse thread is stopped");
    }


    @Override
    public void run(){
        while(!exitFlag.get()){

        }
    }
}
