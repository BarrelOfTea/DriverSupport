package com.barreloftea.driversupport.presentation.ui.fragments.devicesflow

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.barreloftea.driversupport.R
import com.barreloftea.driversupport.databinding.FragmentDevicesBluetoothDeviceBinding
import com.barreloftea.driversupport.domain.processor.common.Constants
import com.barreloftea.driversupport.domain.usecases.interfaces.BlueDeviceDiscoveredListener
import com.barreloftea.driversupport.presentation.navutils.navigateSafely
import com.barreloftea.driversupport.presentation.recyclerview.BluetoothDeviceArrayAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InstructionBluetoothFragment(): Fragment(),
    BlueDeviceDiscoveredListener {

    private val viewModel : DevicesSharedViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var binding : FragmentDevicesBluetoothDeviceBinding
    private lateinit var adapter : BluetoothDeviceArrayAdapter
    private lateinit var deviceType : String
    private val reqCodePerm = 1

    private var permissons = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH_CONNECT) else arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!hasPermisions(requireActivity(), permissons)){
            requestPermissions(permissons, reqCodePerm);
        } else {
            viewModel.getConnectedBlueDevices(this)
        }

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

        adapter = BluetoothDeviceArrayAdapter(requireActivity(), R.layout.item_blue_device, ArrayList<BluetoothDevice>())

        binding.listViewBluetoothAvailableDevices.adapter = adapter
        binding.listViewBluetoothAvailableDevices.setOnItemClickListener { parent, view, position, id ->
            viewModel.stopScanning()
            //NOTICE not a safe call on adapter.getItem()
            viewModel.saveBluetoothDevice(deviceType, adapter.getItem(position)!!.name, adapter.getItem(position)!!.address)
            val bundlePut = Bundle()
            bundlePut.putParcelable(deviceType, adapter.getItem(position))
            navController.navigateSafely(R.id.action_bluetooth_to_devices, bundlePut)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
    }

    override fun onDeviceDiscovered(device: BluetoothDevice?) {
        adapter.add(device)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == reqCodePerm && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.getConnectedBlueDevices(this)
        }
    }


    private fun hasPermisions(context: Context, permissions: Array<String>): Boolean {
        for (per in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    per
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

}


/*viewModel.blueDevicesLD.observe(this){data ->
    data?.let {
        adapter.setData(it)
    }
}*/

/*when {
    ContextCompat.checkSelfPermission(
        requireActivity(),
        Manifest.permission.BLUETOOTH_CONNECT
    ) == PackageManager.PERMISSION_GRANTED -> {
        // You can use the API that requires the permission.
    }
    shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_CONNECT) -> {
        Toast.makeText(requireActivity(), "we need it to connect to your band", Toast.LENGTH_SHORT).show()
}
    else -> {
        // You can directly ask for the permission.
        // The registered ActivityResultCallback gets the result of this request.
        requestPermissionLauncher.launch(permissons)
    }
}*/



/*private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions: Map<String, Boolean> ->
    if (permissions[Manifest.permission.BLUETOOTH_CONNECT]==true && permissions[Manifest.permission.ACCESS_FINE_LOCATION]==true) {
        viewModel.getConnectedBlueDevices(this)
    } else {
        // Explain to the user that the feature is unavailable because the
        // feature requires a permission that the user has denied. At the
        // same time, respect the user's decision. Don't link to system
        // settings in an effort to convince the user to change their
        // decision.
        Toast.makeText(requireActivity(), "we need it to connect to your band", Toast.LENGTH_SHORT).show()
    }
}*/




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