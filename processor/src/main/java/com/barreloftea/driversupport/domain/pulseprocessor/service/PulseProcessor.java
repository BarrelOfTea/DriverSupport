package com.barreloftea.driversupport.domain.pulseprocessor.service;

import android.Manifest;
import android.app.Notification;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.barreloftea.driversupport.domain.processor.Processor;
import com.barreloftea.driversupport.domain.processor.common.Constants;
import com.barreloftea.driversupport.domain.processor.common.ImageBuffer;
import com.barreloftea.driversupport.domain.processor.interfaces.PulseListener;
import com.barreloftea.driversupport.domain.processor.interfaces.StateListener;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.ActionCallback;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.BluetoothRepository;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.HeartRateNotifyListener;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.PulseRepository;
import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

import java.util.concurrent.atomic.AtomicBoolean;

public class PulseProcessor extends Thread {

    private static final String TAG = PulseProcessor.class.getSimpleName();

    private AtomicBoolean exitFlag = new AtomicBoolean(false);
    private Processor processor;
    PulseListener pulseListener;

    private int NORMAL_PULSE;
    private int lastPulse;

    BluetoothRepository bluetoothRepository;
    PulseRepository pulseRepository;
    SharedPrefRepository sharedPrefRepository;
    BluetoothDevice band;
    Context context;

    public PulseProcessor(BluetoothRepository bluetoothRepository, PulseRepository pulseRepository, SharedPrefRepository sharedPrefRepository) {
        this.bluetoothRepository = bluetoothRepository;
        this.pulseRepository = pulseRepository;
        this.sharedPrefRepository = sharedPrefRepository;
    }

    public void init(String address, Processor p, PulseListener pl){
        prepare(address);
        processor = p;
        context = processor.context;
        pulseListener = pl;

        NORMAL_PULSE = sharedPrefRepository.getAverPulse();
        lastPulse = NORMAL_PULSE;
    }

    public void stopAsync() {
        exitFlag.set(true);
        interrupt();
        Log.v("aaa", "pulse thread is stopped");
    }

    public void prepare(String address) {
        band = bluetoothRepository.getDevice(address);

        Log.d(TAG, "band's data is" + band.getName());
    }

    public void connect(){
        Log.d(TAG,"attempting to connect..");
        pulseRepository.connect(band, context, new ActionCallback() {
            @Override
            public void onSuccess(Object data) {
                Log.d(TAG,"connect success");
                setHeartNotifyListener();
                startListening();
            }

            @Override
            public void onFail(int errorCode, String msg) {
                Log.d(TAG,"connect fail, code:"+errorCode+",mgs:"+msg);
            }
        });
    }

    private void setHeartNotifyListener(){
        pulseRepository.setHeartListener(new HeartRateNotifyListener() {
            @Override
            public void onNotify(int heartRate) {
                if (heartRate < NORMAL_PULSE){
                    processor.setBandState(Constants.SLEEPING);
                }
                Log.v(TAG, "pulse is " + heartRate);
                pulseListener.onPulseReceived(heartRate);
                lastPulse = heartRate;
            }
        });
    }

    private void startListening(){
        pulseRepository.startHeartRateScanner();
    }


    @Override
    public void run(){
        Log.v(TAG, "pulse thread started");
        connect();
        while(!exitFlag.get()){

        }
        band = null;
        pulseRepository = null;
        bluetoothRepository = null;
        exitFlag = null;
    }

    public void setNormalPulse(){
        NORMAL_PULSE = lastPulse;
        sharedPrefRepository.saveAverPulse(lastPulse);
    }
}
