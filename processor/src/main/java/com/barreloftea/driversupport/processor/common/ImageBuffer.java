package com.barreloftea.driversupport.processor.common;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

import com.barreloftea.driversupport.cameraservice.interfaces.FrameListener;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImageBuffer {

    private static ImageBuffer imageBuffer;
    public ImageBuffer(){}

    FrameListener frameListener;

    public static synchronized ImageBuffer getInstance() {
        if (imageBuffer == null) {
            imageBuffer = new ImageBuffer();
        }
        return imageBuffer;
    }

    public synchronized void setFrameListener(FrameListener f){
        frameListener = f;
    }

    public synchronized void unsetFrameListener(){
        frameListener = null;
    }

    public static ArrayBlockingQueue<Bitmap> imageQueue = new ArrayBlockingQueue<>(200);
    public static AtomicBoolean isProcessorRunning = new AtomicBoolean(false);

    public synchronized void setFrame(Bitmap b){
        if (frameListener!=null)
            frameListener.onFrame(b);
    }

}
