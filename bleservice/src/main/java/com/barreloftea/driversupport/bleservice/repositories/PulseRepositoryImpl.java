package com.barreloftea.driversupport.bleservice.repositories;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.barreloftea.driversupport.bleservice.miband.MiBand;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.ActionCallback;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.HeartRateNotifyListener;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.PulseRepository;

public class PulseRepositoryImpl implements PulseRepository {

    private MiBand miBand;
    @Override
    public void connect(BluetoothDevice device, Context context, ActionCallback actionCallback) {
        miBand = new MiBand(context);
        miBand.connect(device, actionCallback);
    }

    @Override
    public void setHeartListener(HeartRateNotifyListener listener) {
        miBand.setHeartRateScanListener(listener);
    }


    @Override
    public void startHeartRateScanner() {
        miBand.startHeartRateScan();
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