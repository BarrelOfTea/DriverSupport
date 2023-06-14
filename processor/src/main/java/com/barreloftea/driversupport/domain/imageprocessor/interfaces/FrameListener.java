package com.barreloftea.driversupport.domain.imageprocessor.interfaces;

import android.graphics.Bitmap;

public interface FrameListener {
    void onFrame(Bitmap bitmap);
    //void onPulse(int pulse);
}
