package com.barreloftea.driversupport.presentation.ui.fragments.devicesflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barreloftea.driversupport.R
import com.barreloftea.driversupport.databinding.FragmentDevicesBinding
import com.barreloftea.driversupport.domain.models.Device
import com.barreloftea.driversupport.domain.models.WiFiDeviceM
import com.barreloftea.driversupport.domain.processor.common.Constants
import com.barreloftea.driversupport.presentation.navutils.activityNavController
import com.barreloftea.driversupport.presentation.navutils.navigateSafely
import com.barreloftea.driversupport.presentation.recyclerview.DeviceAdapter
import com.barreloftea.driversupport.presentation.recyclerview.DeviceViewHolderClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DevicesFragment(): Fragment(),
    DeviceViewHolderClickListener {

    private val TAG = DevicesFragment::class.java.simpleName

    private lateinit var binding : FragmentDevicesBinding
    private lateinit var adapter : DeviceAdapter
    private val viewModel : DevicesSharedViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.devicesLD.observe(this){data ->
            data?.let {
                adapter.setData(it)
                //
                adapter.notifyDataSetChanged()
            }
        }
        viewModel.buttonEnableLD.observe(this){enable ->
            if (enable) binding.buttonGetStarted.isEnabled = true
        }
        viewModel.updateDevices()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDevicesBinding.inflate(inflater, container, false)

        val levelsRecyclerView: RecyclerView = binding.recyclerViewDevices
        val layoutManager = LinearLayoutManager(activity)
        levelsRecyclerView.layoutManager = layoutManager
        val scrollposition =
            (levelsRecyclerView.layoutManager as LinearLayoutManager?)!!.findFirstCompletelyVisibleItemPosition()
        levelsRecyclerView.scrollToPosition(scrollposition)
        adapter = DeviceAdapter(this)
        binding.recyclerViewDevices.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonGetStarted.setOnClickListener {
            var bundle = Bundle()
            bundle.putBoolean("startnew", true)

//            var devices = adapter.data
//            bundle.putBoolean(Constants.BAND, (devices[1].isSaved))
//            bundle.putBoolean(Constants.LED, (devices[2].isSaved))

            activityNavController().navigateSafely(R.id.action_global_mainFlowFragment, bundle)
        }
        navController = findNavController()
    }

    override fun onViewHolderClick(device : Device) {
        if (device.isSaved){
            viewModel.deleteDevice(device.type)
        } else {
            if (device.type.equals(Constants.TYPE_CAMERA)){
                var bundle = Bundle()
                if (device is WiFiDeviceM) {
                    //TODO try passing Parcelable
                    bundle.putString(Constants.WIFI_NAME, device.name)
                    bundle.putString(Constants.RTSP_LINK, device.rtsp_link)
                    bundle.putString(Constants.RTSP_USERNAME, device.username)
                    bundle.putString(Constants.RTSP_PASSWORD, device.password)
                }
                navController.navigateSafely(R.id.action_devices_to_camera, bundle)
            } else if(device.type.equals(Constants.TYPE_BAND)) {
                val bundle = Bundle()
                bundle.putString(Constants.TYPE, Constants.TYPE_BAND)
                navController.navigateSafely(R.id.action_devices_to_bluetooth, bundle)
            } else if(device.type.equals(Constants.TYPE_LED)){
                val bundle = Bundle()
                bundle.putString(Constants.TYPE, Constants.TYPE_LED)
                navController.navigateSafely(R.id.action_devices_to_bluetooth, bundle)
            }
        }
    }
}



/*private fun startService(){
    var serviceIntent = Intent(activity, DriverSupportService::class.java)
    activity?.startService(serviceIntent)
}*/