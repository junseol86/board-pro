package ko.hyeonmin.boardpro.activities

import android.app.Activity
import android.os.Bundle
import android.view.Window
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.parts.LabelPanel
import ko.hyeonmin.boardpro.utils.Caches

class ConsoleActivity : Activity() {

    var caches: Caches? = null

    var labelPanel: LabelPanel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_console)

        caches = Caches(this)
        println(caches?.formsJson)
        labelPanel = LabelPanel(this)
    }
}
