package ko.hyeonmin.boardpro.activities

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Window
import com.google.gson.Gson
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.models.Form
import ko.hyeonmin.boardpro.utils.Caches

/**
 * Created by junse on 2017-11-16.
 */
class LabelEditActivity: Activity() {

    var caches: Caches? = null

    var editLabelRV: RecyclerView? = null
    var editLabelLM: RecyclerView.LayoutManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_label_edit)

        caches = Caches(this)
        var form = Gson().fromJson(caches!!.tempFormJson, Form::class.java)

        editLabelRV = findViewById(R.id.editLabelRV)
        editLabelLM = LinearLayoutManager(this)

        editLabelRV?.layoutManager = editLabelLM
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}