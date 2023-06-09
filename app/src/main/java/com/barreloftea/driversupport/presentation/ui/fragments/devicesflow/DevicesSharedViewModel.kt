package com.barreloftea.driversupport.presentation.ui.fragments.devicesflow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.barreloftea.driversupport.domain.models.Device
import com.barreloftea.driversupport.domain.usecases.GetSavedDevicesUC
import com.barreloftea.driversupport.domain.usecases.SaveWiFiDeviceUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DevicesSharedViewModel @Inject constructor(
    getSavedDevicesUC : GetSavedDevicesUC,
    saveWiFiDeviceUC: SaveWiFiDeviceUC
) : ViewModel() {

    private val getSavedDevices : GetSavedDevicesUC
    private val saveWiFiDevice : SaveWiFiDeviceUC


    var devicesLD : MutableLiveData<Array<Device>> = MutableLiveData()

    init {
        this.getSavedDevices = getSavedDevicesUC
        this.saveWiFiDevice = saveWiFiDeviceUC
    }

    fun updateDevices(){
        devicesLD.value = getSavedDevices.execute()
    }

    fun saveWiFiParams(deviceName : String, link : String, username : String, password : String){
        saveWiFiDevice.execute(deviceName, link, username, password)
    }

}