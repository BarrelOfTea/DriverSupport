package com.barreloftea.driversupport.cameraservice.interfaces;

import java.nio.ByteBuffer;

public interface FrameListener {
    void onFrame(ByteBuffer byteBuffer);
}
