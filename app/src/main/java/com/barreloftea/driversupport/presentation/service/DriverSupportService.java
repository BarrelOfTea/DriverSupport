package com.barreloftea.driversupport.presentation.service;

import static com.barreloftea.driversupport.app.App.CHANNEL_ID;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.barreloftea.driversupport.R;
import com.barreloftea.driversupport.domain.processor.common.Constants;
import com.barreloftea.driversupport.presentation.ui.activity.MainActivity;
import com.barreloftea.driversupport.domain.processor.Processor;


import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DriverSupportService extends Service {

    private final DriverSupportBinder binder = new DriverSupportBinder();

    public static final String TAG = DriverSupportService.class.getSimpleName();

    public MutableLiveData<Integer> cameraStateSleepingLD = new MutableLiveData<>(Constants.AWAKE);
    public MutableLiveData<Integer> bandStateSleepingLD = new MutableLiveData<>(Constants.AWAKE);


    @Inject
    Processor processor;


    @Override
    public void onCreate() {
        super.onCreate();
        //TODO consider how to reset params if changed on every Get Started click
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "service is started");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Your status is monitored")
                .setContentText("Open app to see details and configure devices")
                .setSmallIcon(R.drawable.ds)
                .setContentIntent(pendingIntent)
                .build();
        startService();
        startForeground(1, notification);
        Log.v(TAG, "service is created");

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
        return binder;
    }


    public class DriverSupportBinder extends Binder {
        public DriverSupportService getService(){
            return DriverSupportService.this;
        }
    }

    public void startService(){
        processor.setName("processor thread");
        processor.init(this);
        if (!processor.isAlive())
            processor.start();
    }

    public void setEOP(){
        processor.imageProcessor.onEOPupdate();
    }

}

