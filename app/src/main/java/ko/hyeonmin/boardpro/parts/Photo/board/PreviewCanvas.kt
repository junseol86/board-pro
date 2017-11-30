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

        // 먼저 디폴트 사이즈로 그리기를 시도한 뒤
        val firstTry = boardDrawer.draw(activity!!.forms!![0], activity!!.photoPanel!!.boardSetting, canvas!!, width.toFloat(), height.toFloat(), 1f)
        // 보드가 화면보다 크면 이에 맞도록 줄여서 다시 그린다.
        if (!firstTry.third) {
            if ((firstTry.first / width.toFloat()) > (firstTry.second / height.toFloat())) {
                val scale = (width.toFloat() / firstTry.first) * 0.9f
                boardDrawer.draw(activity!!.forms!![0], activity!!.photoPanel!!.boardSetting, canvas!!, width.toFloat(), height.toFloat(), scale)
            } else {
                val scale = (height.toFloat() / firstTry.second) * 0.95f
                boardDrawer.draw(activity!!.forms!![0], activity!!.photoPanel!!.boardSetting, canvas!!, width.toFloat(), height.toFloat(), scale)
            }
        }
    }

}