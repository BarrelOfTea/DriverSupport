package com.barreloftea.driversupport.domain.pulseprocessor.service;

import android.bluetooth.BluetoothDevice;
import android.provider.ContactsContract;
import android.util.Log;

import com.barreloftea.driversupport.domain.processor.Processor;
import com.barreloftea.driversupport.domain.processor.common.ImageBuffer;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.ActionCallback;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.BluetoothRepository;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.HeartRateNotifyListener;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.PulseRepository;

import java.util.concurrent.atomic.AtomicBoolean;

public class PulseProcessor extends Thread{

    private static final String TAG = PulseProcessor.class.getSimpleName();

    private AtomicBoolean exitFlag = new AtomicBoolean(false);
    private Processor processor;
    ImageBuffer imageBuffer;

    private int NORMAL_PULSE = 90;

    BluetoothRepository bluetoothRepository;
    PulseRepository pulseRepository;
    BluetoothDevice band;
    public PulseProcessor(BluetoothRepository bluetoothRepository, PulseRepository pulseRepository){
        this.bluetoothRepository = bluetoothRepository;
        this.pulseRepository = pulseRepository;
        imageBuffer = ImageBuffer.getInstance();
    }

    public void stopAsync(){
        exitFlag.set(true);
        interrupt();
        Log.v("aaa", "pulse thread is stopped");
    }

    public void prepare(String address){
        band = bluetoothRepository.getDevice(address);
    }

    private void connect(){
        pulseRepository.connect(band, processor.context, new ActionCallback() {
            @Override
            public void onSuccess(Object data) {
                Log.v(TAG, "connected successfully");
            }

            @Override
            public void onFail(int errorCode, String msg) {
                Log.v(TAG, "could not connect");
            }
        }, new HeartRateNotifyListener() {
            @Override
            public void onNotify(int heartRate) {
                if (heartRate < NORMAL_PULSE){
                    processor.setBandState(Processor.SLEEPING);
                }
                imageBuffer.updatePulse(heartRate);
            }
        });
    }

    private void startListening(){
        pulseRepository.startHeartRateScanner();
    }


    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public void run(){
        connect();
        startListening();
        while(!exitFlag.get()){

        }
    }
}
