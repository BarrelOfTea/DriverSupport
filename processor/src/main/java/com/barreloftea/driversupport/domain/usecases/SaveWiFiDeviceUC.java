package com.barreloftea.driversupport.domain.usecases;

import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

public class SaveWiFiDeviceUC {

    SharedPrefRepository repository;

    public SaveWiFiDeviceUC(SharedPrefRepository repository){
        this.repository = repository;
    }

    public void execute(String deviceName, String link, String username, String password){
        repository.saveWiFiDevice(deviceName, link, username, password);
    }

}
