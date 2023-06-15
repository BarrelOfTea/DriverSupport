package com.barreloftea.driversupport.bleservice.repositories;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.barreloftea.driversupport.bleservice.miband.MiBand;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.ActionCallback;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.HeartRateNotifyListener;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.PulseRepository;

public class PulseRepositoryImpl implements PulseRepository {

    public static final String TAG = PulseRepositoryImpl.class.getSimpleName();

    private MiBand miBand;
    @Override
    public void connect(BluetoothDevice device, Context context, ActionCallback actionCallback) {
        miBand = new MiBand(context);
        miBand.connect(device, actionCallback);
    }

    @Override
    public void setHeartListener(HeartRateNotifyListener listener) {
        miBand.setHeartRateScanListener(listener);
        Log.v(TAG, "heart listener is set");
    }


    @Override
    public void startHeartRateScanner() {
        miBand.startHeartRateScan();
        Log.v(TAG, "heart scanner is started");
    }
}


/*
    @Override
    public void prepare() {

    }

    @Override
    public void setParams(String mac) {

    }

    @Override
    public int getPulse(String cmd) {
        return 0;
    }
 */