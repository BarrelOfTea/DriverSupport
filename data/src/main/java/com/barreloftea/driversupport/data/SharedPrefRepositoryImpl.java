package com.barreloftea.driversupport.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.barreloftea.driversupport.domain.models.BluetoothDeviceM;
import com.barreloftea.driversupport.domain.models.Device;
import com.barreloftea.driversupport.domain.models.WiFiDeviceM;
import com.barreloftea.driversupport.domain.processor.common.Constants;
import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        Log.v("vol", "set shared vol "+v);
        sharedPreferences.edit().putFloat(Constants.SOUND_VOLUME, v).apply();
    }

    @Override
    public float getSavedSoundVolume() {
        Log.v("vol", "get shared vol "+sharedPreferences.getFloat(Constants.SOUND_VOLUME, 0.8f));
        return sharedPreferences.getFloat(Constants.SOUND_VOLUME, 0.8f);
    }

    @Override
    public void saveSignalSoundResID(int resid) {
        Log.v("sound", "set sound shared id is " + resid);
        sharedPreferences.edit().putInt(Constants.SOUND_RES_INT, resid).apply();
    }

    @Override
    public int getSignalSoundResId() {
        Log.v("sound", "get sound id is " + sharedPreferences.getInt(Constants.SOUND_RES_INT, com.barreloftea.driversupport.domain.R.raw.sirena));
        return sharedPreferences.getInt(Constants.SOUND_RES_INT, com.barreloftea.driversupport.domain.R.raw.sirena);
    }

    @Override
    public void deleteAll() {
        sharedPreferences.edit().clear().apply();
    }

    @Override
    public Map<String, Boolean> getAreSignalsOn() {
        Map<String, Boolean> map = new HashMap<>();
        map.put(Constants.IS_SOUND_SIGNAL_ON, sharedPreferences.getBoolean(Constants.IS_SOUND_SIGNAL_ON, true));
        map.put(Constants.IS_LED_SIGNAL_ON, sharedPreferences.getBoolean(Constants.IS_LED_SIGNAL_ON, false));
        return map;
    }

    @Override
    public void setSignalOn(String signal, boolean isSignalOn) {
        sharedPreferences.edit().putBoolean(signal, isSignalOn).apply();
    }


}
