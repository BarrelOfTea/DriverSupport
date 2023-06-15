package com.barreloftea.driversupport.presentation.service;

import static com.barreloftea.driversupport.app.App.CHANNEL_ID;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.barreloftea.driversupport.R;
import com.barreloftea.driversupport.presentation.ui.activity.MainActivity;
import com.barreloftea.driversupport.domain.processor.Processor;


import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DriverSupportService extends Service {

    public static final String TAG = DriverSupportService.class.getSimpleName();

//    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2039;

    @Inject
    Processor processor;


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
        Log.v(TAG, "service is created");

        processor.setName("processor thread");
        processor.init(this);
        processor.start();

        //TODO consider how to reset params if changed on every Get Started click

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "service is started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "service is destroyed");
        super.onDestroy();
        if (processor!=null) processor.stopAsync();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
