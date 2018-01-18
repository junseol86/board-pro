package ko.hyeonmin.boardpro.parts.Camera

import android.util.Size
import android.widget.ImageView
import com.google.gson.Gson
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.CameraActivity
import ko.hyeonmin.boardpro.models.CameraSetting
import ko.hyeonmin.boardpro.parts.Camera.enums.CmrRatio

/**
 * Created by junse on 2017-12-12.
 */
class CameraOptions(val ca: CameraActivity) {
    var cs: CameraSetting = Gson().fromJson(ca.caches!!.cameraSettingJson, CameraSetting::class.java)

    var ratio: CmrRatio = cs.cmrRatio

    var cameraFlashBtn:ImageView? = null
    var cameraPicSizeBtn:ImageView? = null
    var cameraBoardSizeBtn:ImageView? = null

    init {
        cameraFlashBtn = ca.findViewById(R.id.cmrFlash)
        cameraPicSizeBtn = ca.findViewById(R.id.cmrPicSize)
        cameraBoardSizeBtn = ca.findViewById(R.id.cmrBoardSize)
    }

    // 카메라 최대 해상도와 비율을 참고하여 화면사이즈 옵션 설정
    fun setCameraSetting(maxSize: Size) {
        var cameraSize = Gson().fromJson(ca.caches!!.cameraSettingJson, CameraSetting::class.java)
        cameraSize.cameraMaxSize = maxSize
    }
}