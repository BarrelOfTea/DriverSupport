package com.barreloftea.cameraservice;

public class CameraServiceFactory {

    private static CameraService cameraService = null;

    public static synchronized CameraService getCameraService(){
        if (cameraService == null){
            cameraService = new CameraService();
        }
        return cameraService;
    }

}
