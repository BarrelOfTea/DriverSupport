package com.barreloftea.driversupport.presentation.ui.fragments.settingsflow

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.barreloftea.driversupport.R
import com.barreloftea.driversupport.databinding.FragmentSettingsBinding
import com.barreloftea.driversupport.presentation.navutils.navigateSafely
import com.barreloftea.driversupport.presentation.service.DriverSupportService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment: Fragment() {

    private lateinit var binding : FragmentSettingsBinding
    private lateinit var navController: NavController
    private val viewModel : SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.volumeLD.observe(this){vol ->
            vol?.let {
                binding.seekBarSettingsVolume.setProgress(vol, true)
            }
        }
        viewModel.getSoundVolume()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController();

        binding.tVSettingsChangeSound.setOnClickListener{
            navController.navigateSafely(R.id.action_settings_to_sound)
        }

        binding.seekBarSettingsVolume.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.saveSoundVolume(progress)
                Log.v("aaa", progress.toString())
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.tVSettingsDeleteData.setOnClickListener {
            viewModel.deleteData()
        }
    }

}