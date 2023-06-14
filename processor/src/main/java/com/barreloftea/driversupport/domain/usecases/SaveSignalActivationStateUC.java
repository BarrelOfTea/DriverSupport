package com.barreloftea.driversupport.domain.usecases;

import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

import java.util.Map;

public class SaveSignalActivationStateUC {

    SharedPrefRepository repository;
    public SaveSignalActivationStateUC(SharedPrefRepository repository){
        this.repository = repository;
    }

    public void execute(String signalName, boolean isActivated){
        repository.setSignalOn(signalName, isActivated);
    }

}
