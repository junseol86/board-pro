package ko.hyeonmin.boardpro.viewExtension

import android.view.View
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet

/**
 * Created by junse on 2017-12-01.
 */
class ColorVH(context: Context, attrs: AttributeSet): View(context, attrs) {

    var colorStr = "000000"
    var fillPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
    var rect: Rect? = null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        rect = Rect(0, 0, width, height)
        fillPaint.color = Color.parseColor("#$colorStr")
        canvas?.drawRect(rect, fillPaint)
    }
}