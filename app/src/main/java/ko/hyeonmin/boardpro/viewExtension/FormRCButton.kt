package ko.hyeonmin.boardpro.viewExtension

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.constraint.ConstraintLayout
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import ko.hyeonmin.boardpro.R

/**
 * Created by junse on 2017-11-06.
 */

class FormRCButton : ConstraintLayout, View.OnTouchListener {

    var touched = false
    private var buttonRect: Rect = Rect(0, 0, 0, 0)
    private val buttonPaint: Paint = Paint()
    var activity: Activity? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.activity = context as Activity
        this.setOnTouchListener(this)
        isClickable = true
        buttonPaint.style = Paint.Style.FILL
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        buttonRect.set(0, 0, width, height)
        buttonPaint.style = Paint.Style.FILL
        buttonPaint.color = ResourcesCompat.getColor(resources, if (touched) R.color.form_button_on else R.color.form_button_off, null)
        canvas?.drawRect(buttonRect, buttonPaint)
    }

    override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> touched = true
            MotionEvent.ACTION_UP -> touched = false
            MotionEvent.ACTION_CANCEL -> touched = false
        }
        invalidate()
        return true
    }

}