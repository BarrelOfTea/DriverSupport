package com.barreloftea.driversupport.presentation.ui.fragments.mainflow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.barreloftea.driversupport.R
//import org.opencv.android.Utils
//import org.opencv.core.Core
//import org.opencv.core.Mat
//import org.opencv.videoio.VideoCapture
//import org.opencv.videoio.Videoio


/*class MainFlowFragment: Fragment() {

    val rtspLink = "rtsp://192.168.0.1:554/livestream/12"
    lateinit var capture : VideoCapture
    private var binding: FlowFragmentMainBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        System.loadLibrary("opencv_java4")
        System.setProperty("OPENCV_ANDROID_CAPTURE_OPTIONS", "rtsp_transport;udp");
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        System.loadLibrary("opencv_java470");
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        capture = VideoCapture()
        capture.open(rtspLink, Videoio.CAP_ANDROID)
        capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 640.0)
        capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 480.0)
        return inflater.inflate(R.layout.flow_fragment_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FlowFragmentMainBinding.bind(view)

        Thread {
            val frame = Mat()
            while (capture.read(frame)) {
                val bitmap = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888)
                Utils.matToBitmap(frame, bitmap)
                binding.videoView.setImageBitmap(bitmap)
            }
        }.start()

    }


    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

     override fun onDestroy() {
        super.onDestroy()
         if (capture != null) {
            capture.release()
        }
    }



}*/


import android.net.Uri
import android.util.Log
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.alexvas.rtsp.widget.RtspSurfaceView;
import com.barreloftea.driversupport.databinding.FlowFragmentMainBinding
import com.barreloftea.driversupport.databinding.FragmentDevicesBinding
import com.barreloftea.driversupport.presentation.service.DriverSupportService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFlowFragment: Fragment() {

    private var startNewService = false
    private lateinit var binding : FlowFragmentMainBinding


//    private val link = "rtsp://192.168.0.1:554/livestream/12"
//    private val uri = Uri.parse(link)


    private val viewModel : MainViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        arguments?.let{
            if (requireArguments().getBoolean("startnew")) startNewService = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FlowFragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.videoView.setStatusListener(rtspStatusListener)
//
//        if (!binding.videoView.isStarted()) {
//            binding.videoView.init(uri, "", "", "rtsp-client-android")
//            binding.videoView.debug = false
//            binding.videoView.start(true, false)
//        }

        if (startNewService) {
            startService()
            startNewService=false
        }
    }


    private fun startService(){
        var serviceIntent = Intent(activity, DriverSupportService::class.java)
        //TODO consider startForegroundService
        activity?.startService(serviceIntent)

    }

    override fun onPause() {
        super.onPause()
        Log.v("aaa", "mainfragment is paused");
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.v("aaa", "mainfragment is destroyed");
    }
}