package com.barreloftea.driversupport.domain.usecases;

import com.barreloftea.driversupport.domain.models.BluetoothDeviceM;
import com.barreloftea.driversupport.domain.usecases.interfaces.BluetoothRepository;

public class GetConnectedBTDevicesUC {

    private BluetoothRepository repository;

    public GetConnectedBTDevicesUC(BluetoothRepository repository){
        this.repository = repository;
    }

    public BluetoothDeviceM[] execute(){
        return repository.getBluetoothDevices();
    }
}
