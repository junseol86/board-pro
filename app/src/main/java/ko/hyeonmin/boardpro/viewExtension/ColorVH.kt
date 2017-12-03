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

    var isOn = false

    var colorStr = "000000"
    var fillPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
    var bbPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
    var wbPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
    var rect: Rect? = null
    var bbRect: Rect? = null
    var wbRect: Rect? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        rect = Rect(0, 0, width, height)
        fillPaint.color = Color.parseColor("#$colorStr")
        fillPaint.style = Paint.Style.FILL
        canvas?.drawRect(rect, fillPaint)

        if (isOn) {
            bbPaint.style = Paint.Style.STROKE
            bbPaint.color = Color.BLACK
            bbPaint.strokeWidth = 4f
            wbPaint.style = Paint.Style.STROKE
            wbPaint.color = Color.WHITE
            wbPaint.strokeWidth = 4f

            bbRect = Rect(2,2, width - 2, height - 2)
            wbRect = Rect(6, 6, width - 6, height - 6)
            canvas?.drawRect(bbRect, bbPaint)
            canvas?.drawRect(wbRect, wbPaint)
        }
    }
}