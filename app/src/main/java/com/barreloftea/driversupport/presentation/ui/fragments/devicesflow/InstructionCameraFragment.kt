package com.barreloftea.driversupport.presentation.ui.fragments.devicesflow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.barreloftea.driversupport.R
import com.barreloftea.driversupport.databinding.FragmentDevicesCameraBinding
import com.barreloftea.driversupport.domain.processor.common.Constants
import com.barreloftea.driversupport.presentation.navutils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstructionCameraFragment(): Fragment() {

    private val TAG = InstructionCameraFragment::class.java.simpleName

    private lateinit var binding: FragmentDevicesCameraBinding
    private val viewModel : DevicesSharedViewModel by viewModels()
    private lateinit var navController: NavController

    private var rtsp_device_name = ""
    private var rtsp_link = ""
    private var rtsp_username = ""
    private var rtsp_password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        bundle?.let {
            rtsp_device_name = bundle.getString(Constants.WIFI_NAME) ?: ""
            rtsp_link = bundle.getString(Constants.RTSP_LINK) ?: ""
            rtsp_username = bundle.getString(Constants.RTSP_USERNAME) ?: ""
            rtsp_password = bundle.getString(Constants.RTSP_PASSWORD) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDevicesCameraBinding.inflate(inflater, container, false)

        navController = findNavController()

        binding.etCameraDeviceName.setText(rtsp_device_name)
        binding.etCameraRtspLink.setText(rtsp_link)
        binding.etCameraRtspUsername.setText(rtsp_username)
        binding.etCameraRtspPassword.setText(rtsp_password)

        binding.btCameraSave.setOnClickListener {
            viewModel.saveWiFiParams(
                binding.etCameraDeviceName.text.toString(),
                binding.etCameraRtspLink.text.toString(),
                binding.etCameraRtspUsername.text.toString(),
                binding.etCameraRtspPassword.text.toString()
            )
            navController.navigateSafely(R.id.action_camera_to_devices)
        }

        return binding.root
    }

}