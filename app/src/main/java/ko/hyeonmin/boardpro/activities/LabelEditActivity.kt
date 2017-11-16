package ko.hyeonmin.boardpro.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Window
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.utils.Caches

/**
 * Created by junse on 2017-11-16.
 */
class LabelEditActivity: Activity() {

    var caches: Caches? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_label_edit)

        caches = Caches(this)
        Log.d("CACHED", caches!!.tempFormJson)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}