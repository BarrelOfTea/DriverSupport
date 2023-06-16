package com.barreloftea.driversupport.presentation.ui.fragments.mainflow

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.barreloftea.driversupport.domain.processor.common.Constants
import com.barreloftea.driversupport.domain.usecases.GetSignalsActivationStateUC
import com.barreloftea.driversupport.domain.usecases.SaveSignalActivationStateUC
import com.barreloftea.driversupport.presentation.service.DriverSupportService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//TODO make constructors lok kotlin like
@HiltViewModel
class MainViewModel @Inject constructor(
    getSignalsActivationStateUC: GetSignalsActivationStateUC,
    saveSignalActivationStateUC: SaveSignalActivationStateUC
) : ViewModel() {

    private val getSignalsOn : GetSignalsActivationStateUC
    private val saveSignalActivationState : SaveSignalActivationStateUC

    var soundSignalOnLD = MutableLiveData<Boolean>()
    var ledSignalOnLD = MutableLiveData<Boolean>()

    init {
        getSignalsOn = getSignalsActivationStateUC
        saveSignalActivationState = saveSignalActivationStateUC
    }


    fun getSignalsOn(){
        val hashMap = getSignalsOn.execute()
        soundSignalOnLD.value = hashMap[Constants.IS_SOUND_SIGNAL_ON]
        ledSignalOnLD.value = hashMap[Constants.IS_LED_SIGNAL_ON]
    }

    fun saveSoundSignalSate(stateOn: Boolean){
        saveSignalActivationState.execute(Constants.IS_SOUND_SIGNAL_ON, stateOn)
    }

    fun saveLedSignalSate(stateOn: Boolean){
        saveSignalActivationState.execute(Constants.IS_LED_SIGNAL_ON, stateOn)
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