package ko.hyeonmin.boardpro.activities

import android.app.Activity
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.StreamConfigurationMap
import android.os.Bundle
import android.util.Size
import android.view.TextureView
import android.view.Window
import ko.hyeonmin.boardpro.R
import java.util.*

/**
 * Created by junse on 2017-12-05.
 */
class CameraActivity: Activity() {

    var cameraId: String? = null

    var txtView: TextureView? = null
    var previewSize: Size? = null
    val surfTxtListener: TextureView.SurfaceTextureListener =
            object: TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) {
                }

                override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {
                }

                override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean {
                    return false
                }

                override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, width: Int, height: Int) {
                    setupCamera(width, height)
                }
            }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_camera)

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
                if (cameraCharacterictics.get(CameraCharacteristics.LENS_FACING) !=
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

}
