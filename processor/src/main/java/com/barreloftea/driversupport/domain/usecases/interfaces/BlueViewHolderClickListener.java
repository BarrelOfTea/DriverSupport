package com.barreloftea.driversupport.domain.usecases.interfaces;

import android.bluetooth.BluetoothDevice;

import com.barreloftea.driversupport.domain.models.BluetoothDeviceM;

public interface BlueViewHolderClickListener {

    void onDeviceDiscovered(BluetoothDevice device);

}
