package com.barreloftea.driversupport.bleservice;



import com.barreloftea.driversupport.domain.models.BluetoothDeviceM;
import com.barreloftea.driversupport.domain.usecases.interfaces.BluetoothRepository;

public class BluetoothRepositoryImpl implements BluetoothRepository {
    @Override
    public BluetoothDeviceM[] getBluetoothDevices() {
        return new BluetoothDeviceM[0];
    }
}
