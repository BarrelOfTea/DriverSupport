package com.barreloftea.driversupport.presentation.ui.fragments.devicesflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.barreloftea.driversupport.R
import com.barreloftea.driversupport.databinding.FragmentDevicesBluetoothDeviceBinding
import com.barreloftea.driversupport.domain.models.BluetoothDeviceM
import com.barreloftea.driversupport.domain.processor.common.Constants
import com.barreloftea.driversupport.domain.usecases.interfaces.BlueViewHolderClickListener
import com.barreloftea.driversupport.presentation.recyclerview.BluetoothDeviceArrayAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstructionBluetoothFragment(): Fragment(),
    BlueViewHolderClickListener {

    private val viewModel : DevicesSharedViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var binding : FragmentDevicesBluetoothDeviceBinding
    private lateinit var adapter : BluetoothDeviceArrayAdapter
    private lateinit var deviceType : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*viewModel.blueDevicesLD.observe(this){data ->
            data?.let {
                adapter.setData(it)
            }
        }*/
        viewModel.getConnectedBlueDevices(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDevicesBluetoothDeviceBinding.inflate(inflater, container, false)

        val bundle = arguments
        bundle?.let {
            deviceType = bundle.getString(Constants.TYPE) ?: ""
        }

        adapter = BluetoothDeviceArrayAdapter(requireActivity(), R.layout.item_blue_device, ArrayList<BluetoothDeviceM>())

        binding.listViewBluetoothAvailableDevices.adapter = adapter
        binding.listViewBluetoothAvailableDevices.setOnItemClickListener { parent, view, position, id ->
            viewModel.stopScanning()
            //NOTICE not a safe call on adapter.getItem()
            viewModel.saveBluetoothDevice(deviceType, adapter.getItem(position)!!.name, adapter.getItem(position)!!.address)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
    }

    override fun onDeviceDiscovered(device: BluetoothDeviceM?) {
        adapter.add(device)
    }

}


/*

val levelsRecyclerView: RecyclerView = binding.recViewBluetoothAvailableDevices
        val layoutManager = LinearLayoutManager(activity)
        levelsRecyclerView.layoutManager = layoutManager
        val scrollposition =
            (levelsRecyclerView.layoutManager as LinearLayoutManager?)!!.findFirstCompletelyVisibleItemPosition()
        levelsRecyclerView.scrollToPosition(scrollposition)
        adapter = BluetoothDeviceArrayAdapter(this)
        levelsRecyclerView.adapter = adapter

 */