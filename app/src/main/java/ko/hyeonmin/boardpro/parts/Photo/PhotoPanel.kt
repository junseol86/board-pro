package ko.hyeonmin.boardpro.parts.Photo

import android.os.Build
import android.support.constraint.ConstraintLayout
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.google.gson.Gson
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity
import ko.hyeonmin.boardpro.models.ItemContent
import ko.hyeonmin.boardpro.parts.Photo.board.BoardSetting
import ko.hyeonmin.boardpro.parts.Photo.board.PreviewCanvas
import ko.hyeonmin.boardpro.parts.Photo.colorPicker.ColorPicker
import ko.hyeonmin.boardpro.viewExtension.BlackButton

/**
 * Created by junse on 2017-11-13.
 */
class PhotoPanel(val activity: ConsoleActivity) {

    private val takePhotoBtn: BlackButton = activity.findViewById(R.id.takePhoto)
    var boardSetting = Gson().fromJson(activity.caches!!.boardSettingJson, BoardSetting::class.java)
    var previewCanvas: PreviewCanvas = activity.findViewById(R.id.previewCanvas)

    val colorPicker = ColorPicker(activity)

    var adjustingAlpha = false
    var topDefaultBar: ConstraintLayout = activity.findViewById(R.id.photoPanelTopDefaultBar)
    var alphaBar: ConstraintLayout = activity.findViewById(R.id.photoPanelAlphaBar)
    var alphaBtn: BlackButton = activity.findViewById(R.id.borderAlphaBtn)
    var alphaOkBtn: BlackButton = activity.findViewById(R.id.alphaOk)
    var alphaTV: TextView = activity.findViewById(R.id.alphaPercentageTV)
    var alphaSeekbar: SeekBar = activity.findViewById(R.id.alphaSeekbar)

    init {

        colorPicker.applyToColorIndicators()

        alphaBtn.setOnTouchListener { view, event ->
            alphaBtn.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                toggleAdjustingAlpha()
            }
            false
        }
        alphaOkBtn.setOnTouchListener { view, event ->
            alphaBtn.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                toggleAdjustingAlpha()
                saveBoardSetting()
            }
            false
        }
        setAlphaPercentText()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alphaSeekbar.min = 0
        }
        alphaSeekbar.max = 100
        alphaSeekbar.progress = boardSetting.bgOpacity
        alphaSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                boardSetting.bgOpacity = p0!!.progress
                previewCanvas.invalidate()
                setAlphaPercentText()
            }
        })

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

    // 보드 배경 투명도 조정 모드
    fun toggleAdjustingAlpha() {
        adjustingAlpha = !adjustingAlpha
        topDefaultBar.visibility = if (adjustingAlpha) View.GONE else View.VISIBLE
        alphaBar.visibility = if (adjustingAlpha) View.VISIBLE else View.GONE
    }

    // 퍼센트 텍스트 표시
    fun setAlphaPercentText() {
        alphaTV.text = "${boardSetting.bgOpacity}%"
    }

    //
    fun saveBoardSetting() {
        activity.caches?.boardSettingJson = Gson().toJson(boardSetting)
    }
}