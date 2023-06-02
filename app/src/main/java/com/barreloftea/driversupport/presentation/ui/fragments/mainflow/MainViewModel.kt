package com.barreloftea.driversupport.presentation.ui.fragments.mainflow

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.barreloftea.driversupport.presentation.service.DriverSupportService
import com.barreloftea.driversupport.processor.common.ImageBuffer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    var imageLD = MutableLiveData<Bitmap>()

    init {
        /*Thread(Runnable {
            while(ImageBuffer.isProcessorRunning.get()) {
                Log.v("aaa", "")
                    imageLD.value = ImageBuffer.imageQueue.poll()
            }
        }).start()*/
    }


    fun startService(activity : FragmentActivity){
        var serviceIntent = Intent(activity, DriverSupportService::class.java)
        //TODO consider startForegroundService
        activity?.startService(serviceIntent)
    }


}