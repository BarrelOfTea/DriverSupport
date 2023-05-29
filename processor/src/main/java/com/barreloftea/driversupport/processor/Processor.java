package com.barreloftea.driversupport.processor;

import android.util.Log;

import java.util.Arrays;

public class Processor extends Thread{

    private boolean exit = false;

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public void stopAsync(){
        interrupt();
        exit = true;
    }


    @Override
    public void run() {
        for (int i=0;i<200;i++) {
            if (exit) break;
            try {
                Log.v("aaa", "This is new message from Processor thread"+i);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }
}
