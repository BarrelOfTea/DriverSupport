package com.barreloftea.driversupport.cameraservice.interfaces;

import android.graphics.Bitmap;
import android.media.Image;

import com.barreloftea.driversupport.models.ImageByteData;
import com.google.mlkit.vision.common.InputImage;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

public interface VideoRepository {

    void prepare();
    void setParams(String l, String pass, String uname);

    ArrayBlockingQueue<InputImage> getVideoQueue();

}
