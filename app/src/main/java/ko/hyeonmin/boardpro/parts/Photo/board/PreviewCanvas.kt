package ko.hyeonmin.boardpro.parts.Photo.board

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import ko.hyeonmin.boardpro.activities.ConsoleActivity
import ko.hyeonmin.boardpro.models.Form

/**
 * Created by junse on 2017-11-28.
 */
class PreviewCanvas(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var activity: ConsoleActivity? = null
    var form: Form? = null
    var boardDrawer = BoardDrawer()

    init {
        this.activity = context as ConsoleActivity
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        boardDrawer.draw(
                activity!!.forms!![0],
                activity!!.photoPanel!!.boardSetting, canvas!!,
                0f,
                width.toFloat(),
                height.toFloat(),
                0.9f,
                false)
    }

}