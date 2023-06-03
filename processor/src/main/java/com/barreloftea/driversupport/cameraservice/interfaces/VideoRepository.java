package com.barreloftea.driversupport.cameraservice.interfaces;

import android.graphics.Bitmap;
import android.media.Image;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

public interface VideoRepository {

    ArrayBlockingQueue<Bitmap> getVideoQueue();

}
