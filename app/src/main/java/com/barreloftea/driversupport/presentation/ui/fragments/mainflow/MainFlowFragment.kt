package com.barreloftea.driversupport.presentation.ui.fragments.mainflow

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.barreloftea.driversupport.R
import com.barreloftea.driversupport.databinding.FlowFragmentMainBinding
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.videoio.VideoCapture
import org.opencv.videoio.Videoio


class MainFlowFragment: Fragment() {

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



}