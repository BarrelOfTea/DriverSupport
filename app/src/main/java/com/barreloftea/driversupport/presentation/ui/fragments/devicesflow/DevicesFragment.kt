package com.barreloftea.driversupport.presentation.ui.fragments.devicesflow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barreloftea.driversupport.R
import com.barreloftea.driversupport.databinding.FragmentDevicesBinding
import com.barreloftea.driversupport.presentation.navutils.activityNavController
import com.barreloftea.driversupport.presentation.navutils.navigateSafely
import com.barreloftea.driversupport.presentation.recyclerview.DeviceAdapter
import com.barreloftea.driversupport.presentation.recyclerview.ViewHolderClickListener
import com.barreloftea.driversupport.presentation.service.DriverSupportService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DevicesFragment(): Fragment(), ViewHolderClickListener {

    private lateinit var binding : FragmentDevicesBinding
    private lateinit var adapter : DeviceAdapter
    private val viewModel : DevicesSharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.devicesLD.observe(this){data ->
            data?.let {
                adapter.setData(it)
            }
        }
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
        levelsRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonGetStarted.setOnClickListener {
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

    override fun onViewHolderClick(id: Int) {
        TODO("Not yet implemented")
    }
}