package ko.hyeonmin.boardpro.parts.Photo

import android.view.MotionEvent
import com.google.gson.Gson
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity
import ko.hyeonmin.boardpro.models.ItemContent
import ko.hyeonmin.boardpro.parts.Photo.board.BoardSetting
import ko.hyeonmin.boardpro.parts.Photo.board.PreviewCanvas
import ko.hyeonmin.boardpro.parts.Photo.colorPicker.ColorPicker
import ko.hyeonmin.boardpro.viewExtension.BlackButton
import ko.hyeonmin.boardpro.viewExtension.CIBg
import ko.hyeonmin.boardpro.viewExtension.CIBorder
import ko.hyeonmin.boardpro.viewExtension.CIText

/**
 * Created by junse on 2017-11-13.
 */
class PhotoPanel(val activity: ConsoleActivity) {

    private val takePhotoBtn: BlackButton = activity.findViewById(R.id.takePhoto)
    var boardSetting = if (activity.caches!!.boardSettingJson == "") BoardSetting() else Gson().fromJson(activity.caches!!.boardSettingJson, BoardSetting::class.java)
    var previewCanvas: PreviewCanvas = activity.findViewById(R.id.previewCanvas)

    val colorPicker = ColorPicker(activity)

    var ciText: CIText = activity.findViewById(R.id.ciText)
    var ciBorder: CIBorder = activity.findViewById(R.id.ciBorder)
    var ciBg: CIBg = activity.findViewById(R.id.ciBg)


    init {

        applyToColorIndicators()

        takePhotoBtn.setOnTouchListener { view, event ->
            takePhotoBtn.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                saveItemContents()
            }
            false
        }
    }

    // 각 항목마다 새로운 내용이 있으면 저장
    private fun saveItemContents() {
        activity.forms!![0].items.filter { it.content != "" }.map {
            val item = it
            var hasItem = false
            activity.formPanel?.itemContents?.map {
                if (it.itemName == item.name) {
                    hasItem = true
                    if (!it.contentList.contains(item.content))
                        it.contentList.add(item.content)
                }
            }
            if (!hasItem) {
                var array = ArrayList<String>()
                array.removeAll(array)
                array.add(item.content)
                activity.formPanel?.itemContents?.add(ItemContent(item.name, array))
            }
        }
        activity.caches?.itemContentsJson = Gson().toJson(activity.formPanel!!.itemContents)
    }

    // 보드의 텍스트, 선, 면 색상 버튼에 선택된 색상을 표시
    fun applyToColorIndicators() {
        ciText.invalidate()
        ciBorder.invalidate()
        ciBg.invalidate()
    }
}