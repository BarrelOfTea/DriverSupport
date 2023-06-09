package com.barreloftea.driversupport.presentation.ui.fragments.devicesflow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.barreloftea.driversupport.domain.models.BluetoothDeviceM
import com.barreloftea.driversupport.domain.models.Device
import com.barreloftea.driversupport.domain.usecases.GetConnectedBTDevicesUC
import com.barreloftea.driversupport.domain.usecases.GetSavedDevicesUC
import com.barreloftea.driversupport.domain.usecases.SaveWiFiDeviceUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DevicesSharedViewModel @Inject constructor(
    getSavedDevicesUC : GetSavedDevicesUC,
    saveWiFiDeviceUC: SaveWiFiDeviceUC,
    getConnectedBTDevicesUC: GetConnectedBTDevicesUC
) : ViewModel() {

    private val getSavedDevices : GetSavedDevicesUC
    private val saveWiFiDevice : SaveWiFiDeviceUC
    private val getConnectedBTDevices : GetConnectedBTDevicesUC


    var devicesLD : MutableLiveData<Array<Device>> = MutableLiveData()
    var blueDevicesLD : MutableLiveData<Array<BluetoothDeviceM>> = MutableLiveData()

    init {
        this.getSavedDevices = getSavedDevicesUC
        this.saveWiFiDevice = saveWiFiDeviceUC
        this.getConnectedBTDevices = getConnectedBTDevicesUC
    }

    fun updateDevices(){
        devicesLD.value = getSavedDevices.execute()
    }

    fun saveWiFiParams(deviceName : String, link : String, username : String, password : String){
        saveWiFiDevice.execute(deviceName, link, username, password)
    }

    fun getConnectedBlueDevices(){
        blueDevicesLD.value = getConnectedBTDevices.execute()
    }

}