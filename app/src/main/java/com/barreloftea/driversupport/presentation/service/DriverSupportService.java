package com.barreloftea.driversupport.presentation.service;

import static com.barreloftea.driversupport.app.App.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.barreloftea.driversupport.R;
import com.barreloftea.driversupport.presentation.ui.activity.MainActivity;
import com.barreloftea.driversupport.presentation.ui.fragments.mainflow.MainFlowFragment;
import com.barreloftea.driversupport.processor.Processor;
import com.barreloftea.driversupport.processor.ProcessorFactory;

import javax.inject.Inject;

public class DriverSupportService extends Service {

    Processor processor;

    @Inject
    public DriverSupportService(Processor p){
        processor = p;
    }


    @Override
    public void onCreate() {
        Log.v("aaa", "service is created");
        super.onCreate();
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
        //processor = ProcessorFactory.getProcessor();
        //TODO consider how to reset params if changed on every Get Started click
        processor.start();
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
