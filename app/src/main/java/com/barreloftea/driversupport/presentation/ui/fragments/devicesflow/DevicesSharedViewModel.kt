package com.barreloftea.driversupport.presentation.ui.fragments.devicesflow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.barreloftea.driversupport.domain.models.BluetoothDeviceM
import com.barreloftea.driversupport.domain.models.Device
import com.barreloftea.driversupport.domain.models.WiFiDeviceM
import com.barreloftea.driversupport.domain.usecases.DeleteSavedDeviceUC
import com.barreloftea.driversupport.domain.usecases.GetConnectedBTDevicesUC
import com.barreloftea.driversupport.domain.usecases.GetSavedDevicesUC
import com.barreloftea.driversupport.domain.usecases.SaveBluetoothDeviceUC
import com.barreloftea.driversupport.domain.usecases.SaveWiFiDeviceUC
import com.barreloftea.driversupport.domain.usecases.interfaces.BlueViewHolderClickListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DevicesSharedViewModel @Inject constructor(
    getSavedDevicesUC : GetSavedDevicesUC,
    saveWiFiDeviceUC: SaveWiFiDeviceUC,
    getConnectedBTDevicesUC: GetConnectedBTDevicesUC,
    saveBluetoothDeviceUC : SaveBluetoothDeviceUC,
    deleteSavedDeviceUC: DeleteSavedDeviceUC
) : ViewModel() {

    private val getSavedDevices : GetSavedDevicesUC
    private val saveWiFiDevice : SaveWiFiDeviceUC
    private val getConnectedBTDevices : GetConnectedBTDevicesUC
    private val saveBluetoothDevice : SaveBluetoothDeviceUC
    private val deleteSavedDevice : DeleteSavedDeviceUC


    var devicesLD : MutableLiveData<Array<Device>> = MutableLiveData()
    var buttonEnableLD : MutableLiveData<Boolean> = MutableLiveData()
    //var blueDevicesLD : MutableLiveData<Array<BluetoothDeviceM>> = MutableLiveData()

    init {
        this.getSavedDevices = getSavedDevicesUC
        this.saveWiFiDevice = saveWiFiDeviceUC
        this.getConnectedBTDevices = getConnectedBTDevicesUC
        this.saveBluetoothDevice = saveBluetoothDeviceUC
        this.deleteSavedDevice = deleteSavedDeviceUC
    }

    fun updateDevices(){
        devicesLD.value = getSavedDevices.execute()
        var devices = devicesLD.value
        var cameraDevice = devices?.get(0)
        if (cameraDevice is WiFiDeviceM){
            if (!cameraDevice.rtsp_link.equals(""))
                buttonEnableLD.value = true
        }
    }

    fun saveWiFiParams(deviceName : String, link : String, username : String, password : String){
        saveWiFiDevice.execute(deviceName, link, username, password)
    }

    fun getConnectedBlueDevices(listener : BlueViewHolderClickListener){
        getConnectedBTDevices.execute(listener)
    }

    fun stopScanning(){
        getConnectedBTDevices.stopScanning()
    }

    fun saveBluetoothDevice(type: String, name: String, address: String){
        saveBluetoothDevice.execute(type, name, address)
    }

    fun deleteDevice(type: String){
        deleteSavedDevice.execute(type)
        updateDevices()
    }
}