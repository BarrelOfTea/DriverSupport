package com.barreloftea.driversupport.domain.pulseprocessor.interfaces;


import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.barreloftea.driversupport.domain.usecases.interfaces.BlueViewHolderClickListener;

public interface BluetoothRepository {

    void getBluetoothDevices(BlueViewHolderClickListener listener);

    void stopScan();
    BluetoothDevice getDevice(String address);

}
