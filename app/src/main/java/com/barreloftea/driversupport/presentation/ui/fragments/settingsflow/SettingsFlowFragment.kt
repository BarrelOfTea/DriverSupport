package com.barreloftea.driversupport.presentation.ui.fragments.settingsflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.barreloftea.driversupport.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFlowFragment(): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.flow_fragment_settings, container, false)
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment_settings) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.settings_graph)
        navController.graph = navGraph
        return view
    }

}