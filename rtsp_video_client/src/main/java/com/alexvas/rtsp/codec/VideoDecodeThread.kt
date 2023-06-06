package com.alexvas.rtsp.codec

import android.media.Image
import android.media.MediaCodec
import android.media.MediaCodec.OnFrameRenderedListener
import android.media.MediaFormat
import android.util.Log
import com.google.android.exoplayer2.util.Util
import com.google.mlkit.vision.common.InputImage
import java.nio.ByteBuffer
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean


class VideoDecodeThread (
        private val mimeType: String,
        private val width: Int,
        private val height: Int,
        private val videoFrameQueue: FrameQueue,
        private val onFrameRenderedListener: OnFrameRenderedListener) : Thread() {

    private var exitFlag: AtomicBoolean = AtomicBoolean(false)
    var videoQueue : ArrayBlockingQueue<InputImage> = ArrayBlockingQueue(60)

    fun stopAsync() {
        if (DEBUG) Log.v(TAG, "stopAsync()")
        exitFlag.set(true)
        // Wake up sleep() code
        interrupt()
    }




    private fun getDecoderSafeWidthHeight(decoder: MediaCodec): Pair<Int, Int> {
        val capabilities = decoder.codecInfo.getCapabilitiesForType(mimeType).videoCapabilities
        return if (capabilities.isSizeSupported(width, height)) {
            Pair(width, height)
        } else {
            val widthAlignment = capabilities.widthAlignment
            val heightAlignment = capabilities.heightAlignment
            Pair(
                Util.ceilDivide(width, widthAlignment) * widthAlignment,
                Util.ceilDivide(height, heightAlignment) * heightAlignment)
        }
    }

    override fun run() {
        if (DEBUG) Log.d(TAG, "$name started")

        try {
            val decoder = MediaCodec.createDecoderByType(mimeType)
            val widthHeight = getDecoderSafeWidthHeight(decoder)
            val format = MediaFormat.createVideoFormat(mimeType, widthHeight.first, widthHeight.second)

            decoder.setOnFrameRenderedListener(onFrameRenderedListener, null)

            if (DEBUG) Log.d(TAG, "Configuring surface ${widthHeight.first}x${widthHeight.second} w/ '$mimeType', max instances: ${decoder.codecInfo.getCapabilitiesForType(mimeType).maxSupportedInstances}")
            decoder.configure(format, null, null, 0)

            // TODO: add scale option (ie: FIT, SCALE_CROP, SCALE_NO_CROP)
            //decoder.setVideoScalingMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)

            decoder.start()
            if (DEBUG) Log.d(TAG, "Started surface decoder")

            val bufferInfo = MediaCodec.BufferInfo()

            // Main loop
            while (!exitFlag.get()) {
                val inIndex: Int = decoder.dequeueInputBuffer(DEQUEUE_INPUT_TIMEOUT_US)
                if (inIndex >= 0) {
                    // fill inputBuffers[inputBufferIndex] with valid data
                    val byteBuffer: ByteBuffer? = decoder.getInputBuffer(inIndex)
                    byteBuffer?.rewind()

                    // Preventing BufferOverflowException
                    // if (length > byteBuffer.limit()) throw DecoderFatalException("Error")

                    val frame = videoFrameQueue.pop()
                    if (frame == null) {
                        Log.d(TAG, "Empty video frame")
                        // Release input buffer
                        decoder.queueInputBuffer(inIndex, 0, 0, 0L, 0)
                    } else {
                        byteBuffer?.put(frame.data, frame.offset, frame.length)
                        decoder.queueInputBuffer(inIndex, frame.offset, frame.length, frame.timestamp, 0)
                    }
                }

                if (exitFlag.get()) break
                when (val outIndex = decoder.dequeueOutputBuffer(bufferInfo, DEQUEUE_OUTPUT_BUFFER_TIMEOUT_US)) {
                    MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> Log.d(TAG, "Decoder format changed: ${decoder.outputFormat}")
                    MediaCodec.INFO_TRY_AGAIN_LATER -> if (DEBUG) Log.d(TAG, "No output from decoder available")
                    else -> {
                        if (outIndex >= 0) {
                            //val outputBuffer: ByteBuffer = decoder.getOutputBuffer(outIndex)!!
                            //
                            var image = decoder.getOutputImage(outIndex)
                            //val bufferFormat: MediaFormat = decoder.getOutputFormat(outIndex)
                            image?.let {

                                videoQueue.offer(InputImage.fromMediaImage(image, 0), 10, TimeUnit.MILLISECONDS)

                                image.close()
                            } ?: run {
                                Log.v("aaa", "image is null")
                            }

                             //NOTICE change that to just offer(buffer) if needed
                            decoder.releaseOutputBuffer(
                                outIndex,
                                //bufferInfo.size != 0 && !exitFlag.get()
                                false
                            )
                            Log.v("aaa", "image sent for processing")
                        }
                    }
                }

                // All decoded frames have been rendered, we can stop playing now
                if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                    if (DEBUG) Log.d(TAG, "OutputBuffer BUFFER_FLAG_END_OF_STREAM")
                    break
                }
            }

            // Drain decoder
            val inIndex: Int = decoder.dequeueInputBuffer(DEQUEUE_INPUT_TIMEOUT_US)
            if (inIndex >= 0) {
                decoder.queueInputBuffer(inIndex, 0, 0, 0L, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
            } else {
                Log.w(TAG, "Not able to signal end of stream")
            }

            decoder.stop()
            decoder.release()
            videoFrameQueue.clear()

        } catch (e: Exception) {
            Log.e(TAG, "$name stopped due to '${e.message}'")
            // While configuring stopAsync can be called and surface released. Just exit.
            if (!exitFlag.get()) e.printStackTrace()
            return
        }

        if (DEBUG) Log.d(TAG, "$name stopped")
    }


    private fun YUV_420_888toNV21(image: Image): ByteArray? {
        val nv21: ByteArray
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()
        nv21 = ByteArray(ySize + uSize + vSize)
        //U and V are swapped
        yBuffer[nv21, 0, ySize]
        vBuffer[nv21, ySize, vSize]
        uBuffer[nv21, ySize + vSize, uSize]
        return nv21
    }


    companion object {
        private val TAG: String = VideoDecodeThread::class.java.simpleName
        private const val DEBUG = false

        private val DEQUEUE_INPUT_TIMEOUT_US = TimeUnit.MILLISECONDS.toMicros(500)
        private val DEQUEUE_OUTPUT_BUFFER_TIMEOUT_US = TimeUnit.MILLISECONDS.toMicros(100)
    }

}

