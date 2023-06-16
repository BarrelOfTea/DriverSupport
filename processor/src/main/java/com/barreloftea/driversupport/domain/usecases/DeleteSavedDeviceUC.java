package com.barreloftea.driversupport.domain.usecases;

import com.barreloftea.driversupport.domain.models.Device;
import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

public class DeleteSavedDeviceUC {

    SharedPrefRepository repository;
    public DeleteSavedDeviceUC(SharedPrefRepository repository){
        this.repository = repository;
    }


    public void execute(String type){
        repository.deleteDevice(type);
    }

}
