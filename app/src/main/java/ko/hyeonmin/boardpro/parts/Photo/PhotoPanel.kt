package ko.hyeonmin.boardpro.parts.Photo

import android.view.MotionEvent
import android.widget.TextView
import com.google.gson.Gson
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity
import ko.hyeonmin.boardpro.models.ItemContent
import ko.hyeonmin.boardpro.parts.Photo.board.BoardSetting
import ko.hyeonmin.boardpro.parts.Photo.board.PreviewCanvas
import ko.hyeonmin.boardpro.viewExtension.BlackButton

/**
 * Created by junse on 2017-11-13.
 */
class PhotoPanel(val activity: ConsoleActivity) {

    private val takePhotoBtn: BlackButton = activity.findViewById(R.id.takePhoto)
    var boardSetting = if (activity.caches!!.boardSettingJson == "") BoardSetting() else Gson().fromJson(activity.caches!!.boardSettingJson, BoardSetting::class.java)
    var previewCanvas: PreviewCanvas = activity.findViewById(R.id.previewCanvas)

    init {

        takePhotoBtn.setOnTouchListener { view, event ->
            takePhotoBtn.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                saveItemContents()
            }
            false
        }
    }

    private fun saveItemContents() {
        activity.forms!![0].items.map {
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
}