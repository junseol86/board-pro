package ko.hyeonmin.boardpro.parts.Camera

import android.widget.ImageView
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.CameraActivity

/**
 * Created by junse on 2017-12-12.
 */
class CameraOptions(val ca: CameraActivity) {
    val R_1_1 = 0
    val R_4_3 = 1
    val R_16_9 = 2
    var ratio = R_4_3

    var cameraFlashBtn:ImageView? = null
    var cameraPicSizeBtn:ImageView? = null
    var cameraBoardSizeBtn:ImageView? = null

    init {

    }

    fun setButtons() {
        cameraFlashBtn = ca.findViewById(R.id.cmrFlash)
        cameraPicSizeBtn = ca.findViewById(R.id.cmrPicSize)
        cameraBoardSizeBtn = ca.findViewById(R.id.cmrBoardSize)

    }

}