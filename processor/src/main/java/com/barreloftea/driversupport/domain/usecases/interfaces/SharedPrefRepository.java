package com.barreloftea.driversupport.domain.usecases.interfaces;

import com.barreloftea.driversupport.domain.models.BluetoothDeviceM;
import com.barreloftea.driversupport.domain.models.Device;
import com.barreloftea.driversupport.domain.models.WiFiDeviceM;

import java.util.Map;

public interface SharedPrefRepository {

    Device[] getSavedDevices();
    void saveWiFiDevice(String deviceName, String link, String username, String password);
    void saveBluetoothDevice(String type, String deviceName, String address);
    void deleteDevice(String type);
    BluetoothDeviceM getSavedBlueDevice(String type);
    WiFiDeviceM getSavedWifiDevice();

    void saveSoundVolume(float v);
    float getSavedSoundVolume();
    void saveSignalSoundResID(int resid);
    int getSignalSoundResId();

    void deleteAll();

    boolean getIsSignalOn(String signalName);
    void setSignalOn(String signal, boolean isSignalOn);
}
