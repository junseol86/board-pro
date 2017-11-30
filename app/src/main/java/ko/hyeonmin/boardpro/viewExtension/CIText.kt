package ko.hyeonmin.boardpro.viewExtension

import android.content.Context
import android.util.AttributeSet

/**
 * Created by junse on 2017-11-30.
 */
class CIText(context: Context, attrs: AttributeSet): ColorIndicator(context, attrs) {
    override fun getColorStr() = if (activity!!.photoPanel != null) activity!!.photoPanel!!.boardSetting.fontColor else "#FFFFFF"
}