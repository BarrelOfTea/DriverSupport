package com.barreloftea.driversupport.domain.ledcontroller.service;

import com.barreloftea.driversupport.domain.ledcontroller.interfaces.LedRepository;

public class LedController {

    private LedRepository ledRepository;

    public LedController(LedRepository r){
        ledRepository = r;
        ledRepository.setParams("19:05:20:31:C4:4E");
        ledRepository.prepare();
    }

    public void sendOnCmd(){

    }

    public void sendOffCmd(){

    }

    public void init(){
        //TODO
    }

}
