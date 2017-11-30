package ko.hyeonmin.boardpro.viewExtension

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ko.hyeonmin.boardpro.activities.ConsoleActivity

/**
 * Created by junse on 2017-11-30.
 */

open class ColorIndicator(context: Context, attrs: AttributeSet): View(context, attrs) {

    var activity: ConsoleActivity? = null
    var strokePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
    var fillPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        strokePaint.color = Color.WHITE
        fillPaint.color = Color.parseColor(getColorStr())

        canvas?.drawCircle(width / 2f, height / 2f, width / 2f - 4f, fillPaint)
        canvas?.drawCircle(width / 2f, height / 2f, width / 2f - 4f, strokePaint)
    }

    init {
        this.activity = context as ConsoleActivity
        setWillNotDraw(false)

        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = 2f
        fillPaint.style = Paint.Style.FILL
    }

    open fun getColorStr(): String = "#FFFFFF"

}