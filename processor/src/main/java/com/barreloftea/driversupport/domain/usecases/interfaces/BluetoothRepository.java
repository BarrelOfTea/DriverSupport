package com.barreloftea.driversupport.domain.usecases.interfaces;


import com.barreloftea.driversupport.domain.models.BluetoothDeviceM;

public interface BluetoothRepository {

    BluetoothDeviceM[] getBluetoothDevices();

}
