package com.barreloftea.driversupport.presentation.ui.fragments.mainflow

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.barreloftea.driversupport.R
import com.barreloftea.driversupport.databinding.FlowFragmentMainBinding
import com.barreloftea.driversupport.domain.imageprocessor.interfaces.FrameListener
import com.barreloftea.driversupport.domain.processor.common.Constants
import com.barreloftea.driversupport.domain.processor.common.ImageBuffer
import com.barreloftea.driversupport.domain.processor.interfaces.PulseListener
import com.barreloftea.driversupport.presentation.service.DriverSupportService
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainFlowFragment: Fragment(),
    FrameListener, PulseListener {

    private val TAG = MainFlowFragment::class.java.simpleName

    //when newServiceState is 0-no need to start 1-need to start 2 - already started
    private var newServiceRequired = false
    private var isReceiverRegistered = false
    private lateinit var binding : FlowFragmentMainBinding
    private val viewModel : MainViewModel by viewModels()
    private lateinit var imageBuffer: ImageBuffer
    private lateinit var holder : SurfaceHolder

    private lateinit var driverSupportService: DriverSupportService
    private var isBound = false
    private val dsConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, b: IBinder?) {
            val binder = b as DriverSupportService.DriverSupportBinder
            driverSupportService = binder.service
            isBound = true
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (newServiceRequired){
                val newServiceIntent = Intent(requireActivity(), DriverSupportService::class.java)
                ContextCompat.startForegroundService(requireActivity(), newServiceIntent)
                newServiceRequired=false
                Log.v(TAG, "RESTARTED SERVICE AFTER DESTROYING")
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let{
            if (requireArguments().getBoolean("startnew")) newServiceRequired = true
        }

        viewModel.soundSignalOnLD.observe(this){isOn ->
            isOn?.let {
                binding.switchSoundSignal.isChecked = it
            }
        }
        viewModel.ledSignalOnLD.observe(this){isOn ->
            isOn?.let {
                binding.switchLightSignal.isChecked = it
            }
        }

        viewModel.getSignalsOn()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FlowFragmentMainBinding.inflate(inflater, container, false)

        binding.switchSoundSignal.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.saveSoundSignalSate(isChecked)
        }

        binding.switchLightSignal.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.saveLedSignalSate(isChecked)
        }

        imageBuffer = ImageBuffer.getInstance()
        imageBuffer.setFrameListener(this)
        binding.tvMainState.setOnClickListener {
            driverSupportService.setEOP()
        }
        holder = binding.videoView.holder
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO
        if (newServiceRequired && !isBound) {
            val serviceIntent = Intent(requireActivity(), DriverSupportService::class.java)
            ContextCompat.startForegroundService(requireActivity(), serviceIntent)
            requireActivity().bindService(serviceIntent, dsConnection, Context.BIND_AUTO_CREATE)

            newServiceRequired=false

            Log.v(TAG, "NEW SERVICE IS CREATED")
        } else if (newServiceRequired && isBound){
            val filter = IntentFilter(Constants.SERVICE_DESTROYED_ACTION)
            requireActivity().registerReceiver(receiver, filter)
            isReceiverRegistered = true
            val serviceIntent = Intent(requireActivity(), DriverSupportService::class.java)
            requireActivity().stopService(serviceIntent)

            Log.v(TAG, "DESTROYING SERVICE TO RESTART")
        }

        Intent(requireActivity(), DriverSupportService::class.java).also {intent->
            requireActivity().bindService(intent, dsConnection, Context.BIND_AUTO_CREATE)
            Log.v(TAG, "BOUND TO SERVICE")
        }
    }



    override fun onPause() {
        super.onPause()
        Log.v("aaa", "mainfragment is paused");
    }

    override fun onStop() {
        super.onStop()
        Intent(requireActivity(), DriverSupportService::class.java).also { intent ->
            requireActivity().unbindService(dsConnection)
        }
    }

    override fun onDestroy() {
        imageBuffer.unsetFrameListener()
        if (isReceiverRegistered) {
            requireActivity().unregisterReceiver(receiver)
            isReceiverRegistered = false
        }
        Log.v("aaa", "mainfragment is destroyed");
        super.onDestroy()
    }

    override fun onFrame(bitmap: Bitmap?) {
            //binding.videoView.setImageBitmap(bitmap)
        Log.v(TAG, "onFrame is run on thread " + Thread.currentThread().name)
        val canvas = holder.lockCanvas()
        if (canvas != null && bitmap != null) {
            canvas.drawBitmap(bitmap, 0f, 0f, null);
            holder.unlockCanvasAndPost(canvas);
        }
    }



    fun setPrediction(viewButton: Button, isAwake: Boolean){
        if (isAwake){
            viewButton.text = R.string.awake.toString()
            viewButton.setBackgroundResource(R.color.grass_green)
        } else {
            viewButton.text = R.string.sleeping.toString()
            viewButton.setBackgroundResource(R.color.red)
        }
    }

    override fun onPulseReceived(pulse: Int) {
        binding.tvMainPulse.text = pulse.toString()
    }

}


/*

    fun setCameraPredictionSleeping(){
        binding.tvMainCameraPrediction.text = R.string.sleeping.toString()
        binding.tvMainCameraPrediction.setBackgroundResource(R.color.red)
    }

    fun setBandPredictionSleeping(){
        binding.tvMainBandPrediction.text = R.string.sleeping.toString()
        binding.tvMainBandPrediction.setBackgroundResource(R.color.red)
    }

    fun setCameraPredictionAwake(){
        binding.tvMainCameraPrediction.text = R.string.awake.toString()
        binding.tvMainCameraPrediction.setBackgroundResource(R.color.grass_green)
    }

    fun setBandPredictionAwake(){
        binding.tvMainBandPrediction.text = R.string.awake.toString()
        binding.tvMainBandPrediction.setBackgroundResource(R.color.grass_green)
    }

    fun setPredictionSleeping(){
        binding.tvMainState.text = R.string.sleeping.toString()
        binding.tvMainState.setBackgroundResource(R.color.red)
    }

    fun setPredictionAwake(){
        binding.tvMainState.text = R.string.awake.toString()
        binding.tvMainState.setBackgroundResource(R.color.grass_green)
    }

 */


/*Thread{
    val holder = binding.videoView.holder
    val canvas = holder.lockCanvas()
    while(true) {
        val bitmap = imageBuffer.imageQueue.take()
        requireActivity().runOnUiThread {
            if (canvas != null && bitmap != null) {
                canvas.drawBitmap(bitmap, 0f, 0f, null);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}.start()*/


/*requireActivity().runOnUiThread {
    Log.v("aaa", "inside runonuithread block")
    while (ImageBuffer.isProcessorRunning.get()) {
        Log.v("aaa", "image is about to set to an imageview")
        binding.videoView.setImageBitmap(ImageBuffer.imageQueue.poll())
    }
}*/

/*

    override fun onPulse(pulse: Int) {
        binding.tvMainPulse.setText(pulse)
    }
 */

/*private fun startService(){
    var serviceIntent = Intent(activity, DriverSupportService::class.java)
    activity?.startService(serviceIntent)
}*/


//    private val link = "rtsp://192.168.0.1:554/livestream/12"
//    private val uri = Uri.parse(link)



//        binding.videoView.setStatusListener(rtspStatusListener)
//
//        if (!binding.videoView.isStarted()) {
//            binding.videoView.init(uri, "", "", "rtsp-client-android")
//            binding.videoView.debug = false
//            binding.videoView.start(true, false)
//        }

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