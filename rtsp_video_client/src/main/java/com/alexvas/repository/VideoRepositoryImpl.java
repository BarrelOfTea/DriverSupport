package com.alexvas.repository;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.alexvas.rtsp.widget.RtspSurfaceView;
import com.barreloftea.driversupport.domain.imageprocessor.interfaces.VideoRepository;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ArrayBlockingQueue;

public class VideoRepositoryImpl implements VideoRepository {

    RtspSurfaceView rtspSurfaceView;
    private String link;
    private Uri uri;
    private String password;
    private String username;

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
    public void setParams(String l, String pass, String uname){
        link = l;
        username = uname;
        password = pass;
        uri = Uri.parse(link);
    }

    @Override
    public void prepare(){
        if (!rtspSurfaceView.isStarted() && link!=null) {
            rtspSurfaceView.init(uri, username, password);
            rtspSurfaceView.setDebug(false);
        }
    }

    @Override
    public ArrayBlockingQueue<Bitmap> getVideoQueue() {
        ArrayBlockingQueue<Bitmap> videoQueue=null;
        if (!rtspSurfaceView.isStarted()) {
            rtspSurfaceView.start(true, false);
            while (!rtspSurfaceView.getConnected()){}
            videoQueue = rtspSurfaceView.getVideoQueue();
        }
        return videoQueue;
    }

    @Override
    public void stopAsync() {
        rtspSurfaceView.stop();
    }


}
