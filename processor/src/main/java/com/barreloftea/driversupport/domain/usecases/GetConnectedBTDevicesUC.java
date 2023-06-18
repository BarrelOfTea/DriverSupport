package com.barreloftea.driversupport.domain.usecases;

import com.barreloftea.driversupport.domain.usecases.interfaces.BlueDeviceDiscoveredListener;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.BluetoothRepository;

public class GetConnectedBTDevicesUC {

    private BluetoothRepository repository;

    public GetConnectedBTDevicesUC(BluetoothRepository repository){
        this.repository = repository;
    }

    public void execute(BlueDeviceDiscoveredListener listener){
        repository.getBluetoothDevices(listener);
    }

    public void stopScanning(){
        repository.stopScan();
    }
}
