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
import com.barreloftea.driversupport.databinding.FragmentDevicesBluetoothDeviceBinding
import com.barreloftea.driversupport.domain.models.BluetoothDeviceM
import com.barreloftea.driversupport.presentation.recyclerview.BlueViewHolderClickListener
import com.barreloftea.driversupport.presentation.recyclerview.BluetoothDeviceAdapter

class InstructionBluetoothFragment(): Fragment(),
    BlueViewHolderClickListener {

    private val viewModel : DevicesSharedViewModel by viewModels()
    private lateinit var adapter : BluetoothDeviceAdapter
    private lateinit var navController: NavController
    private lateinit var binding : FragmentDevicesBluetoothDeviceBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.blueDevicesLD.observe(this){data ->
            data?.let {
                adapter.setData(it)
            }
        }
        viewModel.getConnectedBlueDevices()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDevicesBluetoothDeviceBinding.inflate(inflater, container, false)

        val levelsRecyclerView: RecyclerView = binding.recViewBluetoothAvailableDevices
        val layoutManager = LinearLayoutManager(activity)
        levelsRecyclerView.layoutManager = layoutManager
        val scrollposition =
            (levelsRecyclerView.layoutManager as LinearLayoutManager?)!!.findFirstCompletelyVisibleItemPosition()
        levelsRecyclerView.scrollToPosition(scrollposition)
        adapter = BluetoothDeviceAdapter(this)
        levelsRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
    }

    override fun onViewHolderClick(device: BluetoothDeviceM?) {
        TODO("Not yet implemented")
    }

}