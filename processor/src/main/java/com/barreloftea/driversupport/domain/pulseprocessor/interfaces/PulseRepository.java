package com.barreloftea.driversupport.domain.pulseprocessor.interfaces;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

public interface PulseRepository {



    void connect(BluetoothDevice device, Context context, ActionCallback actionCallback);

    void setHeartListener(HeartRateNotifyListener listener);

    void startHeartRateScanner();

}



/*

    void prepare();
    void setParams(String mac);
    int getPulse(String cmd);


 */
