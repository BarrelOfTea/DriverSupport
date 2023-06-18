package com.barreloftea.driversupport.domain.pulseprocessor.interfaces;


import android.bluetooth.BluetoothDevice;

import com.barreloftea.driversupport.domain.usecases.interfaces.BlueDeviceDiscoveredListener;

public interface BluetoothRepository {

    void getBluetoothDevices(BlueDeviceDiscoveredListener listener);

    void stopScan();
    BluetoothDevice getDevice(String address);

}
