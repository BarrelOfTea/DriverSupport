package com.barreloftea.driversupport.presentation.ui.fragments.devicesflow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.barreloftea.driversupport.R
import com.barreloftea.driversupport.presentation.navutils.activityNavController
import com.barreloftea.driversupport.presentation.navutils.navigateSafely
import com.barreloftea.driversupport.presentation.service.DriverSupportService

class DevicesFragment(): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_devices, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var button = view.findViewById<Button>(R.id.button_get_started);
        button.setOnClickListener {
            //startService()
            //TODO write logic from viewmodel, then navigate
            var bundle = Bundle()
            bundle.putBoolean("startnew", true)
            activityNavController().navigateSafely(R.id.action_global_mainFlowFragment, bundle)
        }
    }

    private fun startService(){
        var serviceIntent = Intent(activity, DriverSupportService::class.java)
        activity?.startService(serviceIntent)
    }
}