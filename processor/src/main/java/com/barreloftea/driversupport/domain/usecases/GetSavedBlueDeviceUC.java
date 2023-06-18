package com.barreloftea.driversupport.domain.usecases;


import com.barreloftea.driversupport.domain.models.BluetoothDeviceM;
import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

public class GetSavedBlueDeviceUC {
    SharedPrefRepository repository;

    public GetSavedBlueDeviceUC(SharedPrefRepository repository){
        this.repository = repository;
    }

    public BluetoothDeviceM execute(String type){
        return repository.getSavedBlueDevice(type);
    }


}
