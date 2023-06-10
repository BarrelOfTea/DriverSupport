package com.barreloftea.driversupport.bleservice;



import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.util.Log;

import com.barreloftea.driversupport.domain.models.BluetoothDeviceM;
import com.barreloftea.driversupport.domain.usecases.interfaces.BlueViewHolderClickListener;
import com.barreloftea.driversupport.domain.usecases.interfaces.BluetoothRepository;

import java.util.HashMap;

//TODO add permission check for all permissions in the beginning
public class BluetoothRepositoryImpl implements BluetoothRepository {

    private static final String TAG = BluetoothRepositoryImpl.class.getSimpleName();
    private BlueViewHolderClickListener listener;

    final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            Log.d(TAG,
                    "找到附近的蓝牙设备: name:" + device.getName() + ",uuid:"
                            + device.getUuids() + ",add:"
                            + device.getAddress() + ",type:"
                            + device.getType() + ",bondState:"
                            + device.getBondState() + ",rssi:" + result.getRssi());

            BluetoothDeviceM item = new BluetoothDeviceM(device.getName(), device.getAddress(), false, null);
//            if (!devices.containsKey(item)) {
//                devices.put(item, device);
//            }
            listener.onDeviceDiscovered(item);
        }
    };

    @Override
    public void getBluetoothDevices(BlueViewHolderClickListener listener) {
        this.listener = listener;

        //HashMap<String, BluetoothDeviceM> devices = new HashMap<String, BluetoothDeviceM>();
        startScan(scanCallback);

    }

    @Override
    public void stopScan() {
        stopScan(scanCallback);
    }


    public void startScan(ScanCallback callback) {
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
        scanner.startScan(callback);
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
    }
}
