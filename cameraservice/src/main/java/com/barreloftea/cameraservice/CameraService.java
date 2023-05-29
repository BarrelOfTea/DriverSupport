package com.barreloftea.cameraservice;

public class CameraService extends Thread {

    void stopAsync(){
        interrupt();
    }


    @Override
    public void run() {

    }

}
