package com.barreloftea.driversupport.presentation.ui.fragments.settingsflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.barreloftea.driversupport.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFlowFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.flow_fragment_settings, container, false)

    }

}