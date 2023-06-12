package com.barreloftea.driversupport.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.barreloftea.driversupport.domain.models.BluetoothDeviceM;
import com.barreloftea.driversupport.domain.models.Device;
import com.barreloftea.driversupport.domain.models.WiFiDeviceM;
import com.barreloftea.driversupport.domain.processor.common.Constants;
import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

public class SharedPrefRepositoryImpl implements SharedPrefRepository {

    private static final String TAG = SharedPrefRepositoryImpl.class.getSimpleName();

    Context context;
    private final SharedPreferences sharedPreferences;

    public SharedPrefRepositoryImpl(Context context){
        this.context = context.getApplicationContext();
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }



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

    @Override
    public void saveBluetoothDevice(String type, String deviceName, String address) {
        if (type.equals(Constants.TYPE_BAND)) {
            sharedPreferences.edit().putString(Constants.BAND_NAME, deviceName).apply();
            sharedPreferences.edit().putString(Constants.BAND_ADDRESS, address).apply();
        } else if (type.equals(Constants.TYPE_LED)){
            sharedPreferences.edit().putString(Constants.LED_NAME, deviceName).apply();
            sharedPreferences.edit().putString(Constants.LED_ADDRESS, address).apply();
        } else {
            Log.v(TAG, "there is no such device type");
        }
    }

    @Override
    public void saveSoundVolume(float v) {
        sharedPreferences.edit().putFloat(Constants.SOUND_VOLUME, v).apply();
    }

    @Override
    public float getSavedSoundVolume() {
        return sharedPreferences.getFloat(Constants.SOUND_VOLUME, 0.8f);
    }

    @Override
    public void saveSignalSoundResID(int resid) {
        sharedPreferences.edit().putInt(Constants.SOUND_RES_INT, resid).apply();
    }

    @Override
    public int getSignalSoundResId() {
        //TODO handle if resid is 0
        return sharedPreferences.getInt(Constants.SOUND_RES_INT, 0);
    }

    @Override
    public void deleteAll() {
        sharedPreferences.edit().clear().apply();
    }
}
