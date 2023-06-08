package com.barreloftea.driversupport.domain.usecases;

import com.barreloftea.driversupport.domain.models.BluetoothDeviceM;
import com.barreloftea.driversupport.domain.models.Device;
import com.barreloftea.driversupport.domain.models.WiFiDeviceM;
import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

public class GetSavedDevicesUC {

    SharedPrefRepository repository;
    public GetSavedDevicesUC(SharedPrefRepository repository){
        this.repository = repository;
    }


    public Device[] execute(){
        return repository.getSavedDevices();
    }

}
