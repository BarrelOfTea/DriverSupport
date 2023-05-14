package com.example.driversupport.presentation.ui.fragments.devicesflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.driversupport.R

class DevicesFlowFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.flow_fragment_devices, container, false)
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment_devices) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.devices_graph)
//        navController.setGraph(R.navigation.devices_graph)
        navController.graph = navGraph
        return view
    }

}