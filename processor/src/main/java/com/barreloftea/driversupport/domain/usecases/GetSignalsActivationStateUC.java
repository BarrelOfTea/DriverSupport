package com.barreloftea.driversupport.domain.usecases;

import com.barreloftea.driversupport.domain.processor.common.Constants;
import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

import java.util.HashMap;
import java.util.Map;

public class GetSignalsActivationStateUC {

    SharedPrefRepository repository;
    public GetSignalsActivationStateUC(SharedPrefRepository repository){
        this.repository = repository;
    }

    public Map<String, Boolean> execute(){
        Map<String, Boolean> map = new HashMap<>();
        map.put(Constants.IS_SOUND_SIGNAL_ON, repository.getIsSignalOn(Constants.IS_SOUND_SIGNAL_ON));
        map.put(Constants.IS_LED_SIGNAL_ON, repository.getIsSignalOn(Constants.IS_LED_SIGNAL_ON));
        return map;
    }

}
