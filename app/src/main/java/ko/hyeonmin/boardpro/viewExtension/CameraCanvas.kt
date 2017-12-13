package ko.hyeonmin.boardpro.viewExtension

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ko.hyeonmin.boardpro.activities.CameraActivity
import ko.hyeonmin.boardpro.models.Form
import ko.hyeonmin.boardpro.parts.Camera.CameraOptions
import ko.hyeonmin.boardpro.parts.Photo.board.BoardDrawer
import ko.hyeonmin.boardpro.models.BoardSetting
import ko.hyeonmin.boardpro.models.BoardSizePos

/**
 * Created by junse on 2017-12-11.
 */
class CameraCanvas(context: Context, attrs: AttributeSet): View(context, attrs), View.OnTouchListener {
    val ca: CameraActivity = context as CameraActivity
    val co: CameraOptions = ca.co!!
    val boardDrawer: BoardDrawer = BoardDrawer()
    val form: Form = (Gson().fromJson(ca.caches!!.formsJson, object : TypeToken<ArrayList<Form>>() {}.type) as ArrayList<Form>)[0]
    val bs: BoardSetting = Gson().fromJson(ca.caches!!.boardSettingJson, BoardSetting::class.java)
    var bp: BoardSizePos = Gson().fromJson(ca.caches!!.boardSizePosJson, BoardSizePos::class.java)
    var boardBitmap: Bitmap? = null

//    사진찍기 버튼
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

//    화면이 표시될 영역
    var scrW = 0
    var scrH = 0
    var scrLeftX = 0
    var scrTopY = 0

    var boundsAndBitmapSet = false

//    보드가 표시될 좌표
    var brdTop = 0
    var brdBottom = 0
    var brdLeft = 0
    var brdRight = 0

    init {
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

//    화면이 표시될 영역 정하기
    fun setBounds() {
        when (co.ratio) {
            co.R_1_1 -> {
                scrW = height
                scrH = height
                scrLeftX = (width - scrW) / 2
            }
            co.R_4_3 -> {
                if (width.toFloat() / height > 4f / 3) {
                    scrW = (height * 4f / 3).toInt()
                    scrH = height
                    scrLeftX = (width - scrW) / 2
                } else {
                    scrW = width
                    scrH = (width * 3f / 4).toInt()
                    scrTopY = (height - scrH) / 2
                }
            }
            co.R_16_9 -> {
                if (width.toFloat() / height > 16f / 9) {
                    scrW = (height * 16f / 9).toInt()
                    scrH = height
                    scrLeftX = (width - scrW) / 2
                } else {
                    scrW = width
                    scrH = (width * 9f / 16).toInt()
                    scrH = (height - scrH) / 2
                }
            }
        }

    }

    fun setBitmap() {
        boardBitmap = boardDrawer.draw(form, bs, Canvas(), 24f, 0f, 0f, 1f, true)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (!boundsAndBitmapSet) {
            setBounds()
            setBitmap()
            boundsAndBitmapSet = true
        }

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

//        보드 기준을 위, 아래 중 가까운 쪽으로
        if (bp.top < bp.bottom) {
            brdTop = (scrTopY + bp.top).toInt()
            brdBottom = brdTop + boardBitmap!!.height
        } else {
            brdBottom = (scrTopY + scrH - bp.bottom).toInt()
            brdTop = brdBottom - boardBitmap!!.height
        }
//        보드 기준을 왼쪽, 오른쪽 중 가까운 쪽으로
        if (bp.left < bp.right) {
            brdLeft = (scrLeftX + bp.left).toInt()
            brdRight = brdLeft + boardBitmap!!.width
        } else {
            brdRight = (scrLeftX + scrW - bp.right).toInt()
            brdLeft = brdRight - boardBitmap!!.width
        }

        if (boardBitmap != null) {
            canvas?.drawBitmap(boardBitmap, null,
                    Rect(brdLeft, brdTop, brdRight, brdBottom), boardPaint)
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