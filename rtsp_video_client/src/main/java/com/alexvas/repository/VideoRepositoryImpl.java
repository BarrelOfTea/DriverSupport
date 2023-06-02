package com.alexvas.repository;

import android.media.Image;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.alexvas.rtsp.widget.RtspSurfaceView;
import com.barreloftea.driversupport.cameraservice.interfaces.VideoRepository;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class VideoRepositoryImpl implements VideoRepository {

    RtspSurfaceView rtspSurfaceView;
    private String link = "rtsp://192.168.0.1:554/livestream/12";
    private Uri uri = Uri.parse(link);

    private RtspSurfaceView.RtspStatusListener rtspStatusListener = new RtspSurfaceView.RtspStatusListener() {
        @Override
        public void onRtspFirstFrameRendered() {

        }

        @Override
        public void onRtspStatusFailed(@Nullable String message) {

        }

        @Override
        public void onRtspStatusFailedUnauthorized() {

        }

        @Override
        public void onRtspStatusDisconnected() {

        }

        @Override
        public void onRtspStatusConnected() {

        }

        @Override
        public void onRtspStatusConnecting() {

        }
    };

    public VideoRepositoryImpl(RtspSurfaceView surfaceView){
        rtspSurfaceView = surfaceView;
        rtspSurfaceView.setStatusListener(rtspStatusListener);
    }

    @Override
    public ArrayBlockingQueue<ByteBuffer> getVideoQueue() {
        ArrayBlockingQueue<ByteBuffer> videoQueue=null;
        if (!rtspSurfaceView.isStarted()) {
            rtspSurfaceView.init(uri, "", "", "rtsp-client-android");
            rtspSurfaceView.setDebug(false);
            rtspSurfaceView.start(true, false);
            while (!rtspSurfaceView.getConnected()){

            }
            videoQueue = rtspSurfaceView.getVideoQueue();
        }
        return videoQueue;
    }
}
