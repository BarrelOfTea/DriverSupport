package com.barreloftea.driversupport.domain.ledcontroller.interfaces;

public interface LedRepository {
    void prepare();
    void setParams(String mac);
    void sendCmd(String cmd);
}
