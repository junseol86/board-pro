package ko.hyeonmin.boardpro.parts.Camera

import android.widget.ImageView
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.CameraActivity

/**
 * Created by junse on 2017-12-12.
 */
class CameraOptions(val ca: CameraActivity) {
    val cameraFlashBtn:ImageView = ca.findViewById(R.id.cmrFlash)
    val cameraPicSizeBtn:ImageView = ca.findViewById(R.id.cmrPicSize)
    val cameraBoardSizeBtn:ImageView = ca.findViewById(R.id.cmrBoardSize)
}