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
import ko.hyeonmin.boardpro.parts.Camera.enums.CmrRatio

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
    var scrW = 0f
    var scrH = 0f
    var scrLeftX = 0f
    var scrTopY = 0f

    var boundsAndBitmapSet = false

    var fontSize = 24f

//    보드가 표시될 좌표
    var brdTop = 0f
    var brdBottom = 0f
    var brdLeft = 0f
    var brdRight = 0f

    var movingBoard = false
    var movingFromX = 0f
    var movingFromY = 0f

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
            CmrRatio._1_1 -> {
                scrW = height.toFloat()
                scrH = height.toFloat()
                scrLeftX = (width - scrW) / 2
            }
            CmrRatio._4_3 -> {
                if (width.toFloat() / height > 4f / 3) {
                    scrW = height * 4f / 3
                    scrH = height.toFloat()
                    scrLeftX = (width - scrW) / 2
                } else {
                    scrW = width.toFloat()
                    scrH = width * 3f / 4
                    scrTopY = (height - scrH) / 2
                }
            }
            CmrRatio._16_9 -> {
                if (width.toFloat() / height > 16f / 9) {
                    scrW = height * 16f / 9
                    scrH = height.toFloat()
                    scrLeftX = (width - scrW) / 2
                } else {
                    scrW = width.toFloat()
                    scrH = width * 9f / 16
                    scrTopY = (height - scrH) / 2
                }
            }
        }
    }

    fun setBitmap() {
        boardBitmap = boardDrawer.draw(form, bs, Canvas(), fontSize, 0f, 0f, 1f, true)
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


        if (boardBitmap != null) {

            if (brdLeft == 0f && brdTop == 0f && brdRight == 0f && brdBottom == 0f) {
//          보드 기준을 위, 아래 중 가까운 쪽으로
                if (bp.top < bp.bottom) {
                    brdTop = scrTopY + bp.top
                    brdBottom = brdTop + boardBitmap!!.height
                } else {
                    brdBottom = scrTopY + scrH - bp.bottom
                    brdTop = brdBottom - boardBitmap!!.height
                }
//          보드 기준을 왼쪽, 오른쪽 중 가까운 쪽으로
                if (bp.left < bp.right) {
                    brdLeft = scrLeftX + bp.left
                    brdRight = brdLeft + boardBitmap!!.width
                } else {
                    brdRight = scrLeftX + scrW - bp.right
                    brdLeft = brdRight - boardBitmap!!.width
                }
            }

            canvas?.drawBitmap(boardBitmap, null,
                    RectF(brdLeft, brdTop, brdRight, brdBottom), boardPaint)
        }

    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event!!.x in btnLeftX..btnLeftX + btnW && event!!.y in btnTopY..btnTopY + btnH) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                btnArndPaint.alpha = 227
            }
        }
        if (event!!.x in scrLeftX..scrLeftX + scrW && event!!.y in scrTopY..scrTopY + scrH) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                movingBoard = true
                movingFromX = event.x
                movingFromY = event.y
            }
        }
//        보드를 움직일 시
        if (movingBoard && event.action == MotionEvent.ACTION_MOVE) {
//            보드가 좌우로 스크린을 벗어나지 않는다면, 혹은 이미 벗어나 있다면
            if (
                brdLeft < scrLeftX || brdRight > scrLeftX + scrW ||
                (brdLeft + (event.x - movingFromX) >= scrLeftX &&
                brdRight + (event.x - movingFromX) <= scrLeftX + scrW)
                    ) {
//                보드를 좌우로 움직인다
                brdLeft += event.x - movingFromX
                brdRight += event.x - movingFromX
                invalidate()
            }
//            보드가 위아래로 스크린을 벗어나지 않는다면, 혹은 이미 벗어나 있다면
            if (
                brdTop < scrTopY || brdBottom > scrTopY + scrH ||
                (brdTop + (event.y - movingFromY) >= scrTopY &&
                    brdBottom + (event.y - movingFromY) <= scrTopY + scrH)
                    ) {
//                보드를 위아래로 움직인다
                brdTop += event.y - movingFromY
                brdBottom += event.y - movingFromY
            }
            movingFromX = event.x
            movingFromY = event.y
        }
        if (event.action == MotionEvent.ACTION_UP) {
            btnArndPaint.alpha = 200
            movingBoard = false
        }
        invalidate()
        return true
    }

    fun saveSizeAndPos() {
        ca.caches?.boardSizePosJson = Gson().toJson(BoardSizePos(
                fontSize, brdTop - scrTopY, scrTopY + scrH - brdBottom, brdLeft - scrLeftX, scrLeftX + scrW - brdRight
        ))
    }
}