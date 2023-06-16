package com.barreloftea.driversupport.domain.usecases.interfaces;

import com.barreloftea.driversupport.domain.models.Device;

import java.util.Map;

public interface SharedPrefRepository {

    Device[] getSavedDevices();
    void saveWiFiDevice(String deviceName, String link, String username, String password);
    void saveBluetoothDevice(String type, String deviceName, String address);
    void saveSoundVolume(float v);
    float getSavedSoundVolume();

    void saveSignalSoundResID(int resid);
    int getSignalSoundResId();

    void deleteAll();

    Map<String, Boolean> getAreSignalsOn();
    void setSignalOn(String signal, boolean isSignalOn);

    void deleteDevice(String type);

}
