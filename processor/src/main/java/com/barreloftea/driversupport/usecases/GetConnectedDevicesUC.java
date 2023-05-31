package com.barreloftea.driversupport.usecases;

import com.barreloftea.driversupport.models.BluetoothDevice;
import com.barreloftea.driversupport.models.Device;
import com.barreloftea.driversupport.models.WiFiDevice;

public class GetConnectedDevicesUC {

    public Device[] execute(){
        return new Device[]{new BluetoothDevice("xiaomi mi band 4", 255, false),
                new WiFiDevice("xiaomi 70mai m300", 265, true)};
    }

}
