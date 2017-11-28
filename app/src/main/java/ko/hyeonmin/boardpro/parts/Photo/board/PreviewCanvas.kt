package ko.hyeonmin.boardpro.parts.Photo.board

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import ko.hyeonmin.boardpro.activities.ConsoleActivity
import ko.hyeonmin.boardpro.models.Form

/**
 * Created by junse on 2017-11-28.
 */
class PreviewCanvas(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var activity: ConsoleActivity? = null
    var form: Form? = null

    var itemTextList: ArrayList<Paint>? = ArrayList()
    var contentTextList: ArrayList<Paint>? = ArrayList()

    override fun onDraw(canvas: Canvas?) {
        form = activity!!.forms!![0]

        var maxItemWidth = 0f
        var maxContentWidth = 0f
        var lineHeights: ArrayList<Float> = ArrayList()
        var bs: BoardSetting = activity!!.photoPanel!!.boardSetting

        itemTextList?.removeAll(itemTextList!!)
        contentTextList?.removeAll(contentTextList!!)

        form!!.items.map {
            var itemPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
            itemPaint.style = Paint.Style.FILL_AND_STROKE
            itemPaint.textAlign = Paint.Align.LEFT
            itemPaint.color = Color.parseColor(bs.fontColor)
            itemPaint.textSize = bs.fontSize
            var itemW = if (it.name.isNotEmpty()) itemPaint.measureText(it.name) else 0f
            maxItemWidth = if (itemW > maxItemWidth) itemW else maxItemWidth
            itemTextList?.add(itemPaint)

            var contentPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
            contentPaint.style = Paint.Style.FILL_AND_STROKE
            contentPaint.textAlign = Paint.Align.LEFT
            contentPaint.color = Color.parseColor(bs.fontColor)
            contentPaint.textSize = bs.fontSize
            var r = Rect()
            contentPaint.getTextBounds("A가天", 0, 3, r)
            var contentW = if (it.content.isNotEmpty()) contentPaint.measureText(it.content) else 0f
            var contentH = (r.height() * multiplyLineHeight(it.content)).toFloat()
            maxContentWidth = if (contentW > maxContentWidth) contentW else maxContentWidth
            lineHeights.add(contentH)
            contentTextList?.add(contentPaint)
        }

        super.onDraw(canvas)
    }

    init {
        this.activity = context as ConsoleActivity
        setWillNotDraw(false)
    }

    fun multiplyLineHeight(text: String): Int {
        var enterCount = 1
        (0 until text.length).map {
            if (text[it] == '\n')
                enterCount++
        }
        return enterCount
    }

}