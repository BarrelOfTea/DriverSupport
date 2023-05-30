package com.alexvas.repository;

import com.barreloftea.driversupport.cameraservice.interfaces.VideoRepository;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingDeque;

public class VideoRepositoryImpl implements VideoRepository {
    @Override
    public BlockingDeque<ByteBuffer> getVideoQueue() {
        return null;
    }
}
