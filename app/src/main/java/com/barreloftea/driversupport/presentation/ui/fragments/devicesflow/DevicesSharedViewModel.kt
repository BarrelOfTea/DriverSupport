package com.barreloftea.driversupport.presentation.ui.fragments.devicesflow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.barreloftea.driversupport.domain.models.Device
import com.barreloftea.driversupport.domain.usecases.GetSavedDevicesUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DevicesSharedViewModel @Inject constructor(getDevisesUseCase : GetSavedDevicesUC) : ViewModel() {

    val getDevisesUC : GetSavedDevicesUC
    var devicesLD : MutableLiveData<Array<Device>> = MutableLiveData()

    init {
        getDevisesUC = getDevisesUseCase
    }

    public fun updateDevices(){
        devicesLD.value = getDevisesUC.execute()
    }

}