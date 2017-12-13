package ko.hyeonmin.boardpro.parts.Photo.board

import android.graphics.*
import ko.hyeonmin.boardpro.models.BoardSetting
import ko.hyeonmin.boardpro.models.Form

/**
 * Created by junse on 2017-11-29.
 */
class BoardDrawer {

    fun draw(form: Form, bs: BoardSetting, cv: Canvas, fontSize: Float, width: Float, height: Float, scale: Float, getBitmap: Boolean): Bitmap? {

        var canvas: Canvas? = null
        var boardBitmap: Bitmap? = null
        val items = form.items

        var fz = if (fontSize == 0f) 24f else fontSize
        var bdrW = fz / 20f
        var halfBdrW = bdrW / 2f

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.color = Color.parseColor("#${bs.fontColor}")
        textPaint.textSize = fz

//        한 줄의 높이 구하기
        var r = Rect()
        textPaint.getTextBounds("A가天", 0, 3, r)
        textPaint.getTextBounds("A가天", 0, 3, r)
        var singleLH = r.height().toFloat()

        val bgPaint = Paint()
        bgPaint.color = Color.parseColor("#${bs.bgColor}")
        bgPaint.style = Paint.Style.FILL
        bgPaint.alpha = (bs.bgOpacity * 255f / 100f).toInt()

        var borderPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
        borderPaint.color = Color.parseColor("#${bs.borderColor}")

        borderPaint.style = Paint.Style.STROKE

        var maxItemWidth = 0f
        var maxContentWidth = 0f
        var lineHeights: ArrayList<Float> = ArrayList()

        items.map {
//            항목 이름 중 가장 긴 것을 구하여 열 너비에 적용
            maxItemWidth = Math.max(if (it.name.isNotEmpty()) textPaint.measureText(it.name) else 0f, maxItemWidth)
        }
        items.map {
//            항목 내용은 여러 줄일 수 있으므로 줄별로 나누어 구함
            val contentLines = it.content.split('\n')
            val noItemName = it.name == ""
            (0 until contentLines.size).map {
                maxContentWidth = Math.max(textPaint.measureText(contentLines[it]) - (if (noItemName) (maxContentWidth - (fz * 3f)) else 0f), maxContentWidth)
            }
            var contentH = singleLH * contentLines.size * 1.2f + (fz / 3f)
            lineHeights.add(contentH)
        }

        var boardW = maxItemWidth + maxContentWidth + (fz * 2f) + bdrW
        var boardH = lineHeights.map{it + fz}.sum() + bdrW


        //폰트값이 주어지지 않았다면 여기서 값들을 조정
        if (fontSize == 0f) {
            val scale = if (boardW / width * scale > boardH / height * scale) width * scale / boardW else height * scale / boardH

            fz *= scale
            bdrW *= scale
            halfBdrW *= scale
            singleLH *= scale
            maxItemWidth *= scale
            maxContentWidth *= scale
            boardW *= scale
            boardH *= scale
            textPaint.textSize = fz
            borderPaint.strokeWidth = bdrW
            lineHeights = lineHeights.map { it * scale } as ArrayList<Float>
        }

        // Bitmap을 구하기 위함인가, 화면에 그리기 위함인가에 따라 사용할 canvas가 달라짐
        canvas = if (getBitmap) {
            boardBitmap = Bitmap.createBitmap(boardW.toInt(), boardH.toInt(), Bitmap.Config.ARGB_8888)
            Canvas(boardBitmap)
        } else {
            cv
        }

        val x = if (getBitmap) 0f else (width - boardW) / 2f
        val y = if (getBitmap) 0f else (height - boardH) / 2f

        val rect = RectF(x + bdrW / 2f, y + bdrW / 2f, x + boardW - bdrW / 2f, y + boardH - bdrW)

        canvas.drawRect(rect, bgPaint)
        canvas.drawRect(rect, borderPaint)

//        각 행을 그려나갈 때마다 축적되는 높이
        var acumH = 0f

        (0 until items.size).map {
            if (it != items.size && items[it].name != "")
                canvas.drawLine(
                        x + maxItemWidth + fz + halfBdrW,
                        y + acumH + halfBdrW,
                        x + maxItemWidth + fz + halfBdrW,
                        y + acumH + lineHeights[it] + fz + halfBdrW,
                        borderPaint)
            if (it != 0) {
                canvas.drawLine(
                        x + 0f + halfBdrW,
                        y + acumH + halfBdrW,
                        x + boardW + halfBdrW,
                        y + acumH + halfBdrW,
                        borderPaint)
            }
            canvas.drawText(
                    items[it].name,
                    x + (fz / 2) + halfBdrW,
                    y + acumH + (singleLH * 1.2f) + (fz / 2f) + halfBdrW,
                    textPaint)
            val item = items[it]
            val ctntLines = items[it].content.split('\n')
            (0 until ctntLines.size).map {
                canvas.drawText(
                        ctntLines[it],
                        x + (if (item.name == "") fz / 2 else  maxItemWidth + (fz * 1.5f)) + halfBdrW,
                        y + acumH + (singleLH * 1.2f * (it + 1)) + (fz / 2f) + halfBdrW,
                        textPaint)
            }
            acumH += (lineHeights[it] + fz)
        }

        return if (getBitmap) boardBitmap else null
    }
}