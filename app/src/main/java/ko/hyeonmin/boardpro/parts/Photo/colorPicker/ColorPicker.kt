package ko.hyeonmin.boardpro.parts.Photo.colorPicker

import android.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity
import ko.hyeonmin.boardpro.viewExtension.BlackButton
import ko.hyeonmin.boardpro.viewExtension.CIBg
import ko.hyeonmin.boardpro.viewExtension.CIBorder
import ko.hyeonmin.boardpro.viewExtension.CIText

/**
 * Created by junse on 2017-12-01.
 */
class ColorPicker(val activity: ConsoleActivity) {
    val PC_TEXT = 0
    val PC_BORDER = 1
    val PC_BG = 2

    var ciText: CIText = activity.findViewById(R.id.ciText)
    var ciBorder: CIBorder = activity.findViewById(R.id.ciBorder)
    var ciBg: CIBg = activity.findViewById(R.id.ciBg)

    var pickWhich = PC_TEXT

    val colors = arrayOf(
            "000000", "434343", "666666", "999999", "b7b7b7", "cccccc", "d9d9d9", "efefef", "f3f3f3", "ffffff",
            "980000", "ff0000", "ff9900", "ffff00", "00ff00", "00ffff", "4a86e8", "0000ff", "9900ff", "ff00ff",
            "e6b8af", "f4cccc", "fce5cd", "fff2cc", "d9ead3", "d0e0e3", "c9daf8", "cfe2f3", "d9d2e9", "ead1dc",
            "dd7e6b", "ea9999", "f9cb9c", "ffe599", "b6d7a8", "a2c4c9", "a4c2f4", "9fc5e8", "b4a7d6", "d5a6bd",
            "cc4125", "e06666", "f6b26b", "ffd966", "93c47d", "76a5af", "6d9eeb", "6fa8dc", "8e7cc3", "c27ba0",
            "a61c00", "cc0000", "e69138", "f1c232", "6aa84f", "45818e", "3c78d8", "3d85c6", "674ea7", "a64d79",
            "85200c", "990000", "b45f06", "bf9000", "38761d", "134f5c", "1155cc", "0b5394", "351c75", "741b47",
            "5b0f00", "660000", "783f04", "7f6000", "274e13", "0c343d", "1c4587", "073763", "20124d", "4c1130"
    )

    var selectColorRV: RecyclerView? = null
    var adColorPickerBuilder: AlertDialog? = null

    val colorBtnText: BlackButton = activity.findViewById(R.id.textColorBtn)
    val colorBtnBorder: BlackButton = activity.findViewById(R.id.borderColorBtn)
    val colorBtnBg: BlackButton = activity.findViewById(R.id.bgColorBtn)

    init {

        colorBtnText.setOnTouchListener { view, event ->
            colorBtnText.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                startColorPick(PC_TEXT)
            }
            false
        }

        colorBtnBorder.setOnTouchListener { view, event ->
            colorBtnBorder.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                startColorPick(PC_BORDER)
            }
            false
        }

        colorBtnBg.setOnTouchListener { view, event ->
            colorBtnBg.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                startColorPick(PC_BG)
            }
            false
        }

    }

    fun startColorPick(which: Int) {
        pickWhich = which

        selectColorRV = RecyclerView(activity)
        val selectColorLM = GridLayoutManager(activity, 10)
        val selectColorAD = ColorPickAD(activity, this)
        selectColorRV?.layoutManager = selectColorLM
        selectColorRV?.adapter = selectColorAD

        adColorPickerBuilder = AlertDialog.Builder(activity)
                .setMessage(
                       when (pickWhich)  {
                           PC_TEXT -> activity.resources.getString(R.string.colorText)
                           PC_BORDER -> activity.resources.getString(R.string.colorBorder)
                           else -> activity.resources.getString(R.string.colorBg)
                       }
                )
                .create()
        adColorPickerBuilder?.setView(selectColorRV)
        adColorPickerBuilder?.show()
    }

    // 보드의 텍스트, 선, 면 색상 버튼에 선택된 색상을 표시
    fun applyToColorIndicators() {
        ciText.invalidate()
        ciBorder.invalidate()
        ciBg.invalidate()
    }

}