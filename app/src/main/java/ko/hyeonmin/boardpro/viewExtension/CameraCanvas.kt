package ko.hyeonmin.boardpro.viewExtension

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import ko.hyeonmin.boardpro.activities.CameraActivity

/**
 * Created by junse on 2017-12-11.
 */
class CameraCanvas(context: Context, attrs: AttributeSet): View(context, attrs) {
    val ca: CameraActivity = context as CameraActivity

    var btnArW = 0
    val btnH = 280f
    var btnLeftX = 0f
    var btnTopY = 0f
    val btnMargin = 54
    var btnW = 0
    var brnArndR = 20
    val btnPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
    val btnArndPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
    val btnArndBdrPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)

    init {
        btnPaint.style = Paint.Style.FILL
        btnPaint.color = Color.WHITE
        btnArndPaint.style = Paint.Style.FILL
        btnArndPaint.color = Color.WHITE
        btnArndPaint.alpha = 200
        btnArndBdrPaint.style = Paint.Style.STROKE
        btnArndBdrPaint.strokeWidth = 1f
        btnArndBdrPaint.color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (btnArW == 0) {
            btnArW = ((width - (height * 4f / 3)) / 2).toInt()
            btnTopY = (height - btnH) / 2f
            btnW = btnArW - (btnMargin * 2)
            btnLeftX = width - (btnArW - btnMargin.toFloat())
        }

        canvas?.drawRoundRect(RectF(btnLeftX, btnTopY, btnLeftX + btnW, btnTopY + btnH), btnW / 2f, btnW / 2f, btnPaint)
        canvas?.drawRoundRect(RectF(btnLeftX - brnArndR, btnTopY - brnArndR, btnLeftX + btnW + brnArndR, btnTopY + btnH + brnArndR),
                btnW / 2f + brnArndR, btnW / 2f + brnArndR, btnArndPaint)
        canvas?.drawRoundRect(RectF(btnLeftX - brnArndR, btnTopY - brnArndR, btnLeftX + btnW + brnArndR, btnTopY + btnH + brnArndR),
                btnW / 2f + brnArndR, btnW / 2f + brnArndR, btnArndBdrPaint)

        Log.d("HH", btnArW.toString())
        Log.d("HH", btnTopY.toString())
        Log.d("HH", btnW.toString())
    }
}