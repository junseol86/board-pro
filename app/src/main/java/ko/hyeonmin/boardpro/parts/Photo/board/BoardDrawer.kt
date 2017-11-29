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

        val items = form.items.filter { it.content != "" }
        val fz = bs.fontSize * scale

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.color = Color.parseColor(bs.fontColor)
        textPaint.textSize = fz

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
            maxItemWidth = Math.max(if (it.name.isNotEmpty()) textPaint.measureText(it.name) else 0f, maxItemWidth)
        }
        items.map {
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

        if (boardW > width || boardH > height)
            return Triple(boardW, boardH, false)

        val x = (width - boardW) / 2f
        val y = (height - boardH) / 2f

        val rect = Rect(x.toInt() + 0, y.toInt() + 0, x.toInt() + boardW.toInt(), y.toInt() + boardH.toInt())
        canvas.drawRect(rect, bgPaint)
        canvas.drawRect(rect, borderPaint)

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

        return Triple(boardW, boardH, true)
    }
}