package com.barreloftea.driversupport.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.barreloftea.driversupport.domain.models.BluetoothDeviceM;
import com.barreloftea.driversupport.domain.models.Device;
import com.barreloftea.driversupport.domain.models.WiFiDeviceM;
import com.barreloftea.driversupport.domain.processor.common.Constants;
import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

public class SharedPrefRepositoryImpl implements SharedPrefRepository {

    Context context;

    public SharedPrefRepositoryImpl(Context context){
        this.context = context;
    }

    private SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

    @Override
    public Device[] getSavedDevices() {
        Device[] devices = new Device[3];

        devices[0] = new WiFiDeviceM(
                sharedPreferences.getString(Constants.WIFI_NAME, Constants.NO_CAMERA),
                sharedPreferences.getString(Constants.RTSP_LINK, ""),
                sharedPreferences.getString(Constants.RTSP_USERNAME, ""),
                sharedPreferences.getString(Constants.RTSP_PASSWORD, ""),
                sharedPreferences.contains(Constants.WIFI_NAME),
                Constants.TYPE_CAMERA);
        devices[1] = new BluetoothDeviceM(
                sharedPreferences.getString(Constants.BAND_NAME, Constants.NO_BAND),
                sharedPreferences.getString(Constants.BAND_ADDRESS, ""),
                sharedPreferences.contains(Constants.BAND_NAME),
                Constants.TYPE_BAND);
        devices[2] = new BluetoothDeviceM(
                sharedPreferences.getString(Constants.LED_NAME, Constants.NO_LED),
                sharedPreferences.getString(Constants.LED_ADDRESS, ""),
                sharedPreferences.contains(Constants.LED_NAME),
                Constants.TYPE_LED);

        return devices;
    }

    @Override
    public void saveWiFiDevice(String deviceName, String link, String username, String password) {
        sharedPreferences.edit().putString(Constants.WIFI_NAME, deviceName).apply();
        sharedPreferences.edit().putString(Constants.RTSP_LINK, link).apply();
        sharedPreferences.edit().putString(Constants.RTSP_USERNAME, username).apply();
        sharedPreferences.edit().putString(Constants.RTSP_PASSWORD, password).apply();
    }
}