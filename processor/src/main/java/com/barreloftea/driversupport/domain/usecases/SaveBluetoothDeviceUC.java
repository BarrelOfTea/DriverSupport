package com.barreloftea.driversupport.domain.usecases;

import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

public class SaveBluetoothDeviceUC {

    SharedPrefRepository repository;

    public SaveBluetoothDeviceUC(SharedPrefRepository repository){
        this.repository = repository;
    }

    public void execute(String type, String deviceName, String address){
        repository.saveBluetoothDevice(type, deviceName, address);
    }

}
