package com.barreloftea.driversupport.cameraservice.interfaces;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingDeque;

public interface VideoRepository {

    BlockingDeque<ByteBuffer> getVideoQueue();

}
