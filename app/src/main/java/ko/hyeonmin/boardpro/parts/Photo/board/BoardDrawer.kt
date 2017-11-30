package ko.hyeonmin.boardpro.parts.Photo.board

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import ko.hyeonmin.boardpro.models.Form

/**
 * Created by junse on 2017-11-29.
 */
class BoardDrawer {

    fun draw(form: Form, bs: BoardSetting, canvas: Canvas, width: Float, height: Float, scale: Float): Triple<Float, Float, Boolean> {

        val items = form.items
        val fz = bs.fontSize * scale

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.color = Color.parseColor(bs.fontColor)
        textPaint.textSize = fz

//        한 줄의 높이 구하기
        var r = Rect()
        textPaint.getTextBounds("A가天", 0, 3, r)
        textPaint.getTextBounds("A가天", 0, 3, r)
        val singleLH = r.height()

        val bgPaint = Paint()
        bgPaint.color = Color.parseColor(bs.bgColor)
        bgPaint.style = Paint.Style.FILL

        var borderPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
        borderPaint.color = Color.parseColor(bs.borderColor)
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

        val boardW = maxItemWidth + maxContentWidth + (fz * 2f)
        val boardH = lineHeights.map{it + fz}.sum()

//        보드 높이나 너비가 주어진 공간보다 크면 세번째 값에 false를 반환
        if (boardW > width || boardH > height)
            return Triple(boardW, boardH, false)

//        작다면 계속 그린다
        val x = (width - boardW) / 2f
        val y = (height - boardH) / 2f

        val rect = Rect(x.toInt() + 0, y.toInt() + 0, x.toInt() + boardW.toInt(), y.toInt() + boardH.toInt())
        canvas.drawRect(rect, bgPaint)
        canvas.drawRect(rect, borderPaint)

//        각 행을 그려나갈 때마다 축적되는 높이
        var acumH = 0f

        (0 until items.size).map {
            if (it != items.size && items[it].name != "")
                canvas.drawLine(x + maxItemWidth + fz, y + acumH, x + maxItemWidth + fz, y + acumH + lineHeights[it] + fz, borderPaint)
            if (it != 0) {
                canvas.drawLine(x + 0f, y + acumH, x + boardW, y + acumH, borderPaint)
            }
            canvas.drawText(items[it].name, x + (fz / 2), y + acumH + (singleLH * 1.2f) + (fz / 2f), textPaint)
            val item = items[it]
            val ctntLines = items[it].content.split('\n')
            (0 until ctntLines.size).map {
                canvas.drawText(ctntLines[it],
                        x + (if (item.name == "") fz / 2 else  maxItemWidth + (fz * 1.5f)),
                        y + acumH + (singleLH * 1.2f * (it + 1)) + (fz / 2f),
                        textPaint)
            }
            acumH += (lineHeights[it] + fz)
        }

//        그리기에 성공했으니 세번째 값으로 true 반환
        return Triple(boardW, boardH, true)
    }
}