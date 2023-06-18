package com.barreloftea.driversupport.presentation.ui.fragments.mainflow

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.barreloftea.driversupport.domain.processor.common.Constants
import com.barreloftea.driversupport.domain.usecases.GetSavedBlueDeviceUC
import com.barreloftea.driversupport.domain.usecases.GetSignalsActivationStateUC
import com.barreloftea.driversupport.domain.usecases.SaveSignalActivationStateUC
import com.barreloftea.driversupport.presentation.service.DriverSupportService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//TODO make constructors lok kotlin like
@HiltViewModel
class MainViewModel @Inject constructor(
    getSignalsActivationStateUC: GetSignalsActivationStateUC,
    saveSignalActivationStateUC: SaveSignalActivationStateUC,
    getSavedBLueDeviceUC : GetSavedBlueDeviceUC
) : ViewModel() {

    private val getSignalsOn : GetSignalsActivationStateUC
    private val saveSignalActivationState : SaveSignalActivationStateUC
    private val getSavedBLueDevice : GetSavedBlueDeviceUC

    var soundSignalOnLD = MutableLiveData<Boolean>()
    var ledSignalOnLD = MutableLiveData<Boolean>()

    init {
        getSignalsOn = getSignalsActivationStateUC
        saveSignalActivationState = saveSignalActivationStateUC
        getSavedBLueDevice = getSavedBLueDeviceUC
    }

    fun checkLedIsConnected(): Boolean{
        return getSavedBLueDevice.execute(Constants.TYPE_LED).isSaved
    }

    fun getSignalsOn(){
        val hashMap = getSignalsOn.execute()
        soundSignalOnLD.value = hashMap[Constants.IS_SOUND_SIGNAL_ON]
        ledSignalOnLD.value = hashMap[Constants.IS_LED_SIGNAL_ON]
    }

    fun saveSoundSignalSate(stateOn: Boolean){
        if (stateOn){
            saveSignalActivationState.execute(Constants.IS_SOUND_SIGNAL_ON, true)
        } else {
            if (checkLedIsConnected()){
                ledSignalOnLD.value = true
                saveSignalActivationState.execute(Constants.IS_SOUND_SIGNAL_ON, false)
                saveSignalActivationState.execute(Constants.IS_LED_SIGNAL_ON, true)
            } else {
                soundSignalOnLD.value = true
            }
        }
    }

    fun saveLedSignalSate(stateOn: Boolean){
        if (stateOn){
            if (checkLedIsConnected()){
                saveSignalActivationState.execute(Constants.IS_LED_SIGNAL_ON, true)
            } else {
                ledSignalOnLD.value = false
            }
        } else {
            soundSignalOnLD.value = true
            saveSignalActivationState.execute(Constants.IS_SOUND_SIGNAL_ON, true)
            saveSignalActivationState.execute(Constants.IS_LED_SIGNAL_ON, false)
        }
    }

}




//var imageLD = MutableLiveData<Bitmap>()

/*init {
    Thread(Runnable {
        while(ImageBuffer.isProcessorRunning.get()) {
            Log.v("aaa", "")
                imageLD.value = ImageBuffer.imageQueue.poll()
        }
    }).start()

}*/