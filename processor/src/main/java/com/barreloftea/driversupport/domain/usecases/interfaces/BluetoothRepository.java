package com.barreloftea.driversupport.domain.usecases.interfaces;


public interface BluetoothRepository {

    void getBluetoothDevices(BlueViewHolderClickListener listener);

    void stopScan();

}
