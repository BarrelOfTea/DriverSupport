package com.barreloftea.driversupport.domain.pulseprocessor.interfaces;

public interface PulseRepository {

    void prepare();
    void setParams(String mac);
    int getPulse(String cmd);


}
