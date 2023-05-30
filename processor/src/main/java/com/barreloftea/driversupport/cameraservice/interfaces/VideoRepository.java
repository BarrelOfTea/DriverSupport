package com.barreloftea.driversupport.cameraservice.interfaces;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

public interface VideoRepository {

    ArrayBlockingQueue<ByteBuffer> getVideoQueue();

}
