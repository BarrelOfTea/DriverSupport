package com.barreloftea.driversupport.bleservice;



import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.util.Log;

import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.ActionCallback;
import com.barreloftea.driversupport.bleservice.miband.MiBand;
import com.barreloftea.driversupport.domain.models.BluetoothDeviceM;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.HeartRateNotifyListener;
import com.barreloftea.driversupport.domain.usecases.interfaces.BlueViewHolderClickListener;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.BluetoothRepository;

import java.util.HashMap;

//TODO add permission check for all permissions in the beginning
public class BluetoothRepositoryImpl implements BluetoothRepository {

    private static final String TAG = BluetoothRepositoryImpl.class.getSimpleName();
    private BlueViewHolderClickListener listener;

    ScanCallback scanCallback;


    @Override
    public void getBluetoothDevices(BlueViewHolderClickListener listener) {
        this.listener = listener;
        Log.v(TAG, "Set listener for scan callback");

        HashMap<String, BluetoothDevice> devices = new HashMap<>();

        //TODO make scancallback and devices global final and clear hashmap in stopscan()

        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                BluetoothDevice device = result.getDevice();
                Log.d(TAG,
                        "找到附近的蓝牙设备: name:" + device.getName() + ",uuid:"
                                + device.getUuids() + ",add:"
                                + device.getAddress() + ",type:"
                                + device.getType() + ",bondState:"
                                + device.getBondState() + ",rssi:" + result.getRssi());

                if (!devices.containsKey(device.getAddress())) {
                    devices.put(device.getAddress(), device);
                    listener.onDeviceDiscovered(device);
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.e(TAG, "scan failed, error code is " + errorCode);
            }
        };

        startScan(scanCallback);

    }

    @Override
    public void stopScan() {
        stopScan(scanCallback);
    }

    @Override
    public BluetoothDevice getDevice(String address) {
        //"D7:71:B3:98:F8:57"
        return BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
    }


    public void startScan(ScanCallback callback) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (!adapter.isEnabled()) Log.v(TAG, "adapter is not enabled");
        if (null == adapter) {
            Log.e(TAG, "BluetoothAdapter is null");
            return;
        }
        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
        if (null == scanner) {
            Log.e(TAG, "BluetoothLeScanner is null");
            return;
        }
        scanner.startScan(callback);
        Log.v(TAG, "Started scanning");
    }

    public void stopScan(ScanCallback callback) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (null == adapter) {
            Log.e(TAG, "BluetoothAdapter is null");
            return;
        }
        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
        if (null == scanner) {
            Log.e(TAG, "BluetoothLeScanner is null");
            return;
        }
        scanner.stopScan(callback);
        Log.v(TAG, "Stopped scanning");
    }


}
