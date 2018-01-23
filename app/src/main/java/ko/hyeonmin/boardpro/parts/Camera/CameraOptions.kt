package ko.hyeonmin.boardpro.parts.Camera

import android.util.Log
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
    var sizeIdx = -1

    var cameraFlashBtn:ImageView? = null
    var cameraPicSizeBtn:ImageView? = null
    var cameraBoardSizeBtn:ImageView? = null

    val widths = arrayOf(1024, 2048, 3072, 4096)
    var sizes = HashMap<CmrRatio, ArrayList<Pair<Int, Int>>>()
    var size_1_1 = ArrayList<Pair<Int, Int>>()
    var size_4_3 = ArrayList<Pair<Int, Int>>()
    var size_16_9 = ArrayList<Pair<Int, Int>>()

//    해당 기기에서 사용 가능한 이미지 사이즈
    var sizeAvailable = ArrayList<Pair<Int, Int>>()

    init {
        sizes[CmrRatio._1_1] = size_1_1
        sizes[CmrRatio._4_3] = size_4_3
        sizes[CmrRatio._16_9] = size_16_9
        widths.map {
            sizes[CmrRatio._1_1]?.add(Pair(it, it))
            sizes[CmrRatio._4_3]?.add(Pair(it, it * 3 / 4))
            sizes[CmrRatio._16_9]?.add(Pair(it, it * 9 / 16))
        }

        cameraFlashBtn = ca.findViewById(R.id.cmrFlash)
        cameraPicSizeBtn = ca.findViewById(R.id.cmrPicSize)
        cameraBoardSizeBtn = ca.findViewById(R.id.cmrBoardSize)
    }

    // 카메라 최대 해상도와 비율을 참고하여 화면사이즈 옵션 설정
    fun setCameraSetting(maxSize: Size) {
        sizes[cs.cmrRatio]?.map {
            if (it.first < maxSize.width && it.second < maxSize.height )
                sizeAvailable.add(it)
        }

        sizeIdx = if (cs.imageSize == -1) {
//            이미지 사이즈가 저장된 적 없다면, 가능할 시 두 번째로 작은 사이즈
            Math.min(1, sizeAvailable.size)
        } else {
//            이미지 사이즈가 저장된 적 없다면 카메라 최대크기 값 오류가 없는 한 저장된대로
            Math.min(cs.imageSize, sizeAvailable.size)
        }

        Log.d("SETCAMERA", sizeAvailable.toString())
        Log.d("SETCAMERA", sizeIdx.toString())
    }

    fun setButtons() {

    }
}