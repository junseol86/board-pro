package ko.hyeonmin.boardpro.activities

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Window
import android.widget.TextView
import com.google.gson.Gson
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.models.Form
import ko.hyeonmin.boardpro.parts.Label.recyclerParts.EditLabelRCAdapter
import ko.hyeonmin.boardpro.utils.Caches

/**
 * Created by junse on 2017-11-16.
 */
class EditLabelActivity : Activity() {

    var caches: Caches? = null

    var editLabelRV: RecyclerView? = null
    var editLabelAD: EditLabelRCAdapter? = null
    var editLabelLM: RecyclerView.LayoutManager? = null

    var folderNameTV: TextView? = null
    var fileNameTV: TextView? = null

    var form: Form? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_edit_label)

        caches = Caches(this)
        form = Gson().fromJson(caches!!.tempFormJson, Form::class.java)

        editLabelRV = findViewById(R.id.editLabelRV)
        editLabelAD = EditLabelRCAdapter(this)
        editLabelRV?.setHasFixedSize(true)
        editLabelLM = LinearLayoutManager(this)
        editLabelRV?.adapter = editLabelAD

        editLabelRV?.layoutManager = editLabelLM

        folderNameTV = findViewById(R.id.editLabelFolderNameTV)
        fileNameTV = findViewById(R.id.editLabelFileNameTV)

        setFolderFileNameResult()
    }

    // 폴더명이나 파일명이 없는 상황이 발생하지 않도록
    fun secureFolderFileName() {

    }

    fun setFolderFileNameResult() {
        form?.items?.map {
            if (it.folderName) {
                folderNameTV?.text = "폴더명: " + it.name
            }
            if (it.fileName) {
                fileNameTV?.text = "파일명: " + it.name
            }
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}