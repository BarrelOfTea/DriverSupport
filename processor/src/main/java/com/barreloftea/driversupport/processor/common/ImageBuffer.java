package com.barreloftea.driversupport.processor.common;

import android.graphics.Bitmap;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImageBuffer {

    public static ArrayBlockingQueue<Bitmap> imageQueue = new ArrayBlockingQueue<>(200);
    public static AtomicBoolean isProcessorRunning = new AtomicBoolean(false);


}
