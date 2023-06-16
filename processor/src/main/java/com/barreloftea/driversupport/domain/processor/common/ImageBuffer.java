package com.barreloftea.driversupport.domain.processor.common;

import android.graphics.Bitmap;
import android.util.Log;

import com.barreloftea.driversupport.domain.imageprocessor.interfaces.FrameListener;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImageBuffer {

    public static final String TAG = ImageBuffer.class.getSimpleName();

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

    public ArrayBlockingQueue<Bitmap> imageQueue = new ArrayBlockingQueue<>(200);
    public static AtomicBoolean isProcessorRunning = new AtomicBoolean(false);


    public synchronized void setFrame(Bitmap b){
        if (frameListener!=null) {
            frameListener.onFrame(b);
        }
        Log.v(TAG, "image onFrame");
    }


    //NOTICE do we really need synchronized here?
//    public synchronized boolean isListenerSet(){
//        return frameListener!=null;
//    }

}


/*

    ImageProcessor EOPlistener;
    public synchronized void setEOPlistener(ImageProcessor processor){
        EOPlistener = processor;
    }
    public synchronized void updatePulse(int pulse){
        if (frameListener!=null) {
            frameListener.onPulse(pulse);
        }
    }

    public synchronized void updateEOP(){
        if (EOPlistener!=null) {
            EOPlistener.onEOPupdate();
        }
    }

 */