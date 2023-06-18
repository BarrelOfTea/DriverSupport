package com.barreloftea.driversupport.domain.usecases.interfaces;

import android.bluetooth.BluetoothDevice;

public interface BlueDeviceDiscoveredListener {

    void onDeviceDiscovered(BluetoothDevice device);

}
