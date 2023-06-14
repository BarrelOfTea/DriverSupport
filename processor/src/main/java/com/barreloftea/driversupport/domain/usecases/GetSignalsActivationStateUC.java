package com.barreloftea.driversupport.domain.usecases;

import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

import java.util.Map;

public class GetSignalsActivationStateUC {

    SharedPrefRepository repository;
    public GetSignalsActivationStateUC(SharedPrefRepository repository){
        this.repository = repository;
    }

    public Map<String, Boolean> execute(){
        return repository.getAreSignalsOn();
    }

}
