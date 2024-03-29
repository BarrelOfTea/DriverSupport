package com.alexvas.rtsp.codec

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import android.media.MediaCodec
import android.media.MediaCodec.OnFrameRenderedListener
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.util.Log
import android.util.Log.VERBOSE
import com.google.android.exoplayer2.util.Util
import com.google.mlkit.vision.common.InputImage
import java.io.ByteArrayOutputStream
import java.io.IOException
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
    var videoQueue : ArrayBlockingQueue<Bitmap> = ArrayBlockingQueue(10)
    lateinit var bitmap : Bitmap
    val options = BitmapFactory.Options().apply {
        // Set your desired options here
        inJustDecodeBounds = true
    }

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
            //val codecImageFormat = getImageFormatFromCodecType(mime);

            decoder.setOnFrameRenderedListener(onFrameRenderedListener, null)

            if (DEBUG) Log.d(TAG, "Configuring surface ${widthHeight.first}x${widthHeight.second} w/ '$mimeType', max instances: ${decoder.codecInfo.getCapabilitiesForType(mimeType).maxSupportedInstances}")
            decoder.configure(format, null, null, 0)

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
                            //val bufferFormat: MediaFormat = decoder.getOutputFormat(outIndex)
                            //
                            var image = decoder.getOutputImage(outIndex)


                            image?.let {

                                val yuvImage = YuvImage(
                                    YUV_420_888toNV21(image),
                                    ImageFormat.NV21,
                                    480,
                                    360,
                                    null
                                )

                                val stream = ByteArrayOutputStream()
                                yuvImage.compressToJpeg(Rect(0, 0, 480, 360), 80, stream)
                                bitmap = BitmapFactory.decodeByteArray(
                                    stream.toByteArray(),
                                    0,
                                    stream.size(),
                                )
                                try {
                                    stream.close()
                                } catch (e:IOException) {
                                    e.printStackTrace()
                                }

                                bitmap.let {
                                    if (!videoQueue.offer(bitmap)){
                                        videoQueue.poll()
                                        //videoQueue.offer(bitmap, 10, TimeUnit.MILLISECONDS)
                                        videoQueue.add(bitmap)
                                        Log.v("aaa", "image sent for processing")
                                    }

                                }
                                image.close();
                            } ?: run {
                                Log.v("aaa", "image is null")
                            }

                             //NOTICE change that to just offer(buffer) if needed
                            decoder.releaseOutputBuffer(
                                outIndex,
                                //bufferInfo.size != 0 && !exitFlag.get()
                                false
                            )

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

        val yBytes = ByteArray(ySize)
        yBuffer.get(yBytes)
        val uBytes = ByteArray(uSize)
        uBuffer.get(uBytes)
        val vBytes = ByteArray(vSize)
        vBuffer.get(vBytes)

        // Downscale the Y, U, and V planes to the desired resolution
        var downscaledYBytes = downscaleYPlane(yBytes, 1280, 720, 480, 360)
        var downscaledUBytes = downscaleUVPlane(uBytes, 1280 / 2, 720 / 2, 480 / 2, 360 / 2)
        var downscaledVBytes = downscaleUVPlane(vBytes, 1280 / 2, 720 / 2, 480 / 2, 360 / 2)

        // Convert the downscaled YUV data to NV21 format
        nv21 = ByteArray(480 * 360 + (480 / 2) * (360 / 2) * 2)
        System.arraycopy(downscaledYBytes, 0, nv21, 0, downscaledYBytes.size)
        for (i in downscaledVBytes.indices) {
            nv21[downscaledYBytes.size + i * 2] = downscaledVBytes[i]
            nv21[downscaledYBytes.size + i * 2 + 1] = downscaledUBytes[i]
        }

        yBuffer.clear()
        uBuffer.clear()
        vBuffer.clear()


        return nv21
    }

    private fun downscaleYPlane(src: ByteArray, srcWidth: Int, srcHeight: Int,
                                dstWidth: Int, dstHeight: Int): ByteArray {
        val dst = ByteArray(dstWidth * dstHeight)

        for (y in 0 until dstHeight) {
            for (x in 0 until dstWidth) {
                val srcX = x * srcWidth / dstWidth
                val srcY = y * srcHeight / dstHeight
                dst[y * dstWidth + x] = src[srcY * srcWidth + srcX]
            }
        }

        return dst
    }

    private fun downscaleUVPlane(src: ByteArray, srcWidth: Int, srcHeight: Int,
                                 dstWidth: Int, dstHeight: Int): ByteArray {
        val dst = ByteArray(dstWidth * dstHeight)

        for (y in 0 until dstHeight) {
            for (x in 0 until dstWidth) {
                val srcX = x * srcWidth / dstWidth
                val srcY = y * srcHeight / dstHeight
                dst[y * dstWidth + x] = src[srcY * srcWidth + srcX]
            }
        }

        return dst
    }

    companion object {
        private val TAG: String = VideoDecodeThread::class.java.simpleName
        private const val DEBUG = false

        private val DEQUEUE_INPUT_TIMEOUT_US = TimeUnit.MILLISECONDS.toMicros(500)
        private val DEQUEUE_OUTPUT_BUFFER_TIMEOUT_US = TimeUnit.MILLISECONDS.toMicros(100)
    }

}




/*private fun getImageFormatFromCodecType(mimeType: String): Int {
    // TODO: Need pick a codec first, then get the codec info, will revisit for future.
    val codecInfo: MediaCodecInfo = getCodecInfoByType(mimeType)
    //if (VERBOSE) Log.v(TAG, "found decoder: " + codecInfo.getName())
    val colorFormat: Int = selectDecoderOutputColorFormat(codecInfo, mimeType)
    /*if (VERBOSE) Log.v(
        TAG,
        "found decoder output color format: $colorFormat"
    )*/
    return when (colorFormat) {
        MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar ->                 // TODO: This is fishy, OMX YUV420P is not identical as YV12, U and V planes are
            // swapped actually. It should give YV12 if producer is setup first, that is, after
            // Configuring the Surface (provided by ImageReader object) into codec, but this
            // is Chicken-egg issue, do the translation on behalf of driver here:)
            ImageFormat.YV12

        MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar ->                 // same as above.
            ImageFormat.NV21

        else -> colorFormat
    }
}*/


