package com.barreloftea.driversupport.domain.imageprocessor.interfaces;

import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ArrayBlockingQueue;

public interface VideoRepository {

    void prepare();
    void setParams(String l, String pass, String uname);

    ArrayBlockingQueue<InputImage> getVideoQueue();

}
