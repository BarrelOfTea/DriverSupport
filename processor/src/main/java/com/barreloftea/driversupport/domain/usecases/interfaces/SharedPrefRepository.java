package com.barreloftea.driversupport.domain.usecases.interfaces;

import com.barreloftea.driversupport.domain.models.Device;

public interface SharedPrefRepository {

    Device[] getSavedDevices();
    void saveWiFiDevice(String deviceName, String link, String username, String password);

}
