package com.barreloftea.driversupport.presentation.ui.fragments.devicesflow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.barreloftea.driversupport.models.Device
import com.barreloftea.driversupport.usecases.GetConnectedDevicesUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DevicesSharedViewModel @Inject constructor(getDevisesUseCase : GetConnectedDevicesUC) : ViewModel() {

    val getDevisesUC : GetConnectedDevicesUC
    var devicesLD : MutableLiveData<Array<Device>> = MutableLiveData()

    init {
        getDevisesUC = getDevisesUseCase
        devicesLD.value = getDevisesUC.execute()
    }



}