package com.barreloftea.driversupport.cameraservice.interfaces;

import android.graphics.Bitmap;

import java.nio.ByteBuffer;

public interface FrameListener {
    void onFrame(Bitmap bitmap);
}
