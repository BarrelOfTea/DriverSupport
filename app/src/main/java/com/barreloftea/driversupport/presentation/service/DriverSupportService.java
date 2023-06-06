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

import com.barreloftea.driversupport.R;
import com.barreloftea.driversupport.presentation.ui.activity.MainActivity;
import com.barreloftea.driversupport.domain.processor.Processor;


import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DriverSupportService extends Service {

    @Inject
    Processor processor;
    //MainViewModel viewModel = new ViewModelProvider(new ViewModelStore(), new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MainViewModel.class);


    @Override
    public void onCreate() {

        super.onCreate();
        Intent notificationIntent = new Intent(this, MainActivity.class);
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
        processor.start();
        //new CameraService(new VideoRepositoryImpl(new RtspSurfaceView())).start();

        //TODO consider how to reset params if changed on every Get Started click

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("aaa", "service is started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.v("aaa", "service is destroyed");
        super.onDestroy();
        if (processor!=null) processor.stopAsync();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
