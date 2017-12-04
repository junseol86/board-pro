package ko.hyeonmin.boardpro.parts.Photo.colorPicker

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity
import ko.hyeonmin.boardpro.viewExtension.ColorVH

/**
 * Created by junse on 2017-12-01.
 */
class ColorPickAD(val activity: Activity, val cp: ColorPicker): RecyclerView.Adapter<ColorPickAD.ColorPickerVH>() {
    override fun onBindViewHolder(holder: ColorPickerVH?, position: Int) {
        holder?.colorVH?.colorStr = cp.colors[position]

        holder?.colorVH?.isOn =
                if (activity is ConsoleActivity) {
                    when (cp.pickWhich) {
                        cp.PC_TEXT -> activity.photoPanel?.boardSetting?.fontColor == cp.colors[position]
                        cp.PC_BORDER -> activity.photoPanel?.boardSetting?.borderColor == cp.colors[position]
                        else -> activity.photoPanel?.boardSetting?.bgColor == cp.colors[position]
                    }
                } else
                    false
        holder?.colorVH?.setOnClickListener {
            if (activity is ConsoleActivity) {
                when (cp.pickWhich)  {
                    cp.PC_TEXT -> activity.photoPanel?.boardSetting?.fontColor = cp.colors[position]
                    cp.PC_BORDER ->  activity.photoPanel?.boardSetting?.borderColor = cp.colors[position]
                    else -> activity.photoPanel?.boardSetting?.bgColor = cp.colors[position]
                }
                activity.photoPanel?.colorPicker?.applyToColorIndicators()
                activity.photoPanel?.previewCanvas?.invalidate()
                activity.photoPanel?.saveBoardSetting()
                cp.adColorPickerBuilder?.dismiss()
            }
        }
    }

    override fun getItemCount(): Int = cp.colors.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ColorPickerVH =
            ColorPickerVH(LayoutInflater.from(parent!!.context).inflate(R.layout.pick_color_viewholder, null))

    inner class ColorPickerVH(item: View): RecyclerView.ViewHolder(item) {
        var colorVH: ColorVH = item.findViewById(R.id.color_vh)
    }
}