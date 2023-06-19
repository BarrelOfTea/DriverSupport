package com.barreloftea.driversupport.domain.processor.interfaces;

public interface StateListener {
    void onStateChanged(boolean cameraAwake, boolean bandAwake, boolean fullAwake);
}
