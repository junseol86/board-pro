package ko.hyeonmin.boardpro.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.StreamConfigurationMap
import android.os.Bundle
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.Window
import ko.hyeonmin.boardpro.R
import java.util.*

/**
 * Created by junse on 2017-12-05.
 */
class CameraActivity: Activity() {

    val R_1_1 = 0
    val R_4_3 = 1
    val R_16_9 = 2
    var ratio = R_4_3

    var cameraId: String? = null
    var cameraDevice: CameraDevice? = null
    var cameraDeviceStateCallBack: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera_device: CameraDevice?) {
            cameraDevice = camera_device
            createCameraPreviewSession()
        }
        override fun onDisconnected(camera_device: CameraDevice?) {
            camera_device?.close()
            cameraDevice = null
        }
        override fun onError(camera_device: CameraDevice?, p1: Int) {
            camera_device?.close()
            cameraDevice = null
        }

    }
    var previewCaptureRequest: CaptureRequest? = null
    var previewCaptureRequestBuilder: CaptureRequest.Builder? = null
    var cameraCaptureSession: CameraCaptureSession? = null
    var sessionCaptureCallback: CameraCaptureSession.CaptureCallback = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureStarted(session: CameraCaptureSession?, request: CaptureRequest?, timestamp: Long, frameNumber: Long) {
            super.onCaptureStarted(session, request, timestamp, frameNumber)
        }
    }

    var txtView: TextureView? = null
    var previewSize: Size? = null
    val surfTxtListener: TextureView.SurfaceTextureListener =
            object: TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, width: Int, height: Int) {
                    setupCamera(width, height)
                    transformation(width, height)
                    openCamera()
                }
                override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) {
                }
                override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {
                }
                override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean {
                    return false
                }
            }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_camera)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        txtView = findViewById(R.id.textureView)
    }

    override fun onResume() {
        super.onResume()
        if (txtView?.isAvailable!!) {
        } else {
            txtView?.surfaceTextureListener = surfTxtListener
        }
    }

    private fun setupCamera(width: Int, height: Int) {
        val cameraManager: CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            for (camera_id in cameraManager.cameraIdList) {
                val cameraCharacterictics = cameraManager.getCameraCharacteristics(camera_id)
                if (cameraCharacterictics.get(CameraCharacteristics.LENS_FACING) ==
                        CameraCharacteristics.LENS_FACING_FRONT) {
                    continue
                }
                val map: StreamConfigurationMap = cameraCharacterictics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                previewSize = getPreferredPreviewSize(map.getOutputSizes(SurfaceTexture::class.java), width, height)
                cameraId = camera_id
                return
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun getPreferredPreviewSize(mapSizes: Array<Size>, width: Int, height: Int): Size {
        var collectorSizes: ArrayList<Size> = ArrayList()
        mapSizes.map {
            if (width > height) {
                if (it.width > width && it.height > height)
                    collectorSizes.add(it)
            } else {
                if (it.width > height && it.height > width)
                    collectorSizes.add(it)
            }
        }
        if (collectorSizes.size > 0) {
            return Collections.min(collectorSizes, { lhs, rhs ->
                when {
                    lhs.width * lhs.height - rhs.width * rhs.height < 0 -> -1
                    lhs.width * lhs.height - rhs.width * rhs.height == 0 -> 0
                    else -> 1
                }
            }) as Size
        }
        return mapSizes[0]
    }

    @SuppressLint("MissingPermission")
    fun openCamera() {
        var cameraManager: CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraManager.openCamera(cameraId, cameraDeviceStateCallBack, null)
        } catch(e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    fun createCameraPreviewSession() {
        try {
            var surfaceTexture: SurfaceTexture = txtView!!.surfaceTexture
            when (ratio) {
                R_1_1 -> surfaceTexture.setDefaultBufferSize(previewSize!!.height, previewSize!!.height)
                R_4_3 -> surfaceTexture.setDefaultBufferSize((previewSize!!.height.toFloat() * 4 / 3).toInt(), previewSize!!.height)
                else -> surfaceTexture.setDefaultBufferSize((previewSize!!.height.toFloat() * 16 / 9).toInt(), previewSize!!.height)
            }
            var previewSurface = Surface(surfaceTexture)
            previewCaptureRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            previewCaptureRequestBuilder?.addTarget(previewSurface)
            cameraDevice?.createCaptureSession(Arrays.asList(previewSurface), object :CameraCaptureSession.StateCallback() {
                override fun onConfigureFailed(p0: CameraCaptureSession?) {
                }

                override fun onConfigured(session: CameraCaptureSession?) {
                    if (cameraDevice == null) {
                        return
                    }
                    try {
                        previewCaptureRequest = previewCaptureRequestBuilder!!.build()
                        cameraCaptureSession = session
                        cameraCaptureSession?.setRepeatingRequest(
                                previewCaptureRequest,
                                sessionCaptureCallback,
                                null
                        )
                    } catch (e: CameraAccessException) {
                        e.printStackTrace()
                    }
                }
            }, null)

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    fun transformation(width: Int, height: Int) {
        if (previewSize == null || txtView == null)
            return
        var matrix = Matrix()
        val txtRectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        var prvRectF: RectF = when (ratio) {
            R_1_1 -> RectF(0f, 0f, previewSize!!.height.toFloat(), previewSize!!.height.toFloat())
            R_4_3 -> RectF(0f, 0f, previewSize!!.height.toFloat(), previewSize!!.height.toFloat() * 4 / 3)
            else -> RectF(0f, 0f, previewSize!!.height.toFloat(), previewSize!!.height.toFloat() * 16 / 9)
        }
        val centerX = txtRectF.centerX()
        val centerY = txtRectF.centerY()
        prvRectF.offset(centerX - prvRectF.centerX(), centerY - prvRectF.centerY())
        matrix.setRectToRect(txtRectF, prvRectF, Matrix.ScaleToFit.FILL)
        val scale = Math.max(width.toFloat()/previewSize!!.width, height.toFloat()/previewSize!!.height)
        matrix.postScale(scale, scale, centerX, centerY)
        matrix.postRotate(-90f, centerX, centerY)
        txtView?.setTransform(matrix)
    }

}
