package com.barreloftea.driversupport.presentation.service;

import static com.barreloftea.driversupport.app.App.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.alexvas.repository.VideoRepositoryImpl;
import com.alexvas.rtsp.widget.RtspSurfaceView;
import com.barreloftea.driversupport.R;
import com.barreloftea.driversupport.cameraservice.service.CameraService;
import com.barreloftea.driversupport.presentation.ui.fragments.mainflow.MainFlowFragment;
import com.barreloftea.driversupport.processor.Processor;
import com.barreloftea.driversupport.processor.ProcessorFactory;


import java.util.Arrays;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DriverSupportService extends Service {

//    @Inject
//    Processor processor;

//    @Inject
//    public DriverSupportService(Processor p){
//        processor = p;
//    }


    @Override
    public void onCreate() {

        //super.onCreate();
        Intent notificationIntent = new Intent(this, MainFlowFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Your status is monitored")
                .setContentText("Open app to see details and configure devices")
                .setSmallIcon(R.drawable.ds)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        Log.v("aaa", "service is created");
        //processor = new Processor(new CameraService(new VideoRepositoryImpl(new RtspSurfaceView())));
        new CameraService(new VideoRepositoryImpl(new RtspSurfaceView())).start();

        //TODO consider how to reset params if changed on every Get Started click

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("aaa", "service is started");
        //processor.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.v("aaa", "service is destroyed");
        super.onDestroy();
        //if (processor!=null) processor.stopAsync();
        //TODO remember about stopping threads in onDestroys
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
