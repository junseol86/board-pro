package ko.hyeonmin.boardpro.viewExtension

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ko.hyeonmin.boardpro.activities.CameraActivity
import ko.hyeonmin.boardpro.models.Form
import ko.hyeonmin.boardpro.parts.Photo.board.BoardDrawer
import ko.hyeonmin.boardpro.parts.Photo.board.BoardSetting

/**
 * Created by junse on 2017-12-11.
 */
class CameraCanvas(context: Context, attrs: AttributeSet): View(context, attrs), View.OnTouchListener {
    val ca: CameraActivity = context as CameraActivity
    val boardDrawer: BoardDrawer = BoardDrawer()
    val form: Form = (Gson().fromJson(ca.caches!!.formsJson, object : TypeToken<ArrayList<Form>>() {}.type) as ArrayList<Form>)[0]
    val bs: BoardSetting = Gson().fromJson(ca.caches!!.boardSettingJson, BoardSetting::class.java)
    var boardBitmap: Bitmap? = null

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
    val boardPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)

    init {
        boardBitmap = boardDrawer.draw(form, bs, Canvas(), 24f, 0f, 0f, 1f, true)
        invalidate()

        this.setOnTouchListener(this)
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

        if (boardBitmap != null) {
            canvas?.drawBitmap(boardBitmap, null, Rect(300, 300, 300 + boardBitmap!!.width, 300 + boardBitmap!!.height), boardPaint)
        }

    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event!!.x in btnLeftX..btnLeftX + btnW && event!!.y in btnTopY..btnTopY + btnH) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> btnArndPaint.alpha = 227
                MotionEvent.ACTION_UP ->  {
                }
            }
        }
        if (event.action == MotionEvent.ACTION_UP)
            btnArndPaint.alpha = 200
        invalidate()
        return true
    }
}