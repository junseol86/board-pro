package ko.hyeonmin.boardpro.activities

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MotionEvent
import android.view.Window
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.models.Form
import ko.hyeonmin.boardpro.models.Item
import ko.hyeonmin.boardpro.parts.Form.recyclerParts.EditFormRCAdapter
import ko.hyeonmin.boardpro.parts.activityExtension.FormSavingActivity
import ko.hyeonmin.boardpro.utils.Caches
import ko.hyeonmin.boardpro.viewExtension.WhiteButton

/**
 * Created by junse on 2017-11-16.
 */
class EditFormActivity : FormSavingActivity() {

    var editFormAddBtn: WhiteButton? = null
    var editFormSaveBtn: WhiteButton? = null

    var editFormRV: RecyclerView? = null
    var editFormAD: EditFormRCAdapter? = null
    var editFormLM: RecyclerView.LayoutManager? = null

    var folderNameBtn: WhiteButton? = null
    var folderNameTV: TextView? = null
    var fileNameBtn: WhiteButton? = null
    var fileNameTV: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_edit_form)

        caches = Caches(this)
        forms = Gson().fromJson(this.caches?.formsJson, object : TypeToken<ArrayList<Form>>() {}.type)

        editFormAddBtn = findViewById(R.id.editFormAddBtn)
        editFormAddBtn?.setOnTouchListener { view, motionEvent ->
            editFormAddBtn?.onTouch(view, motionEvent)
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                AlertDialog.Builder(this)
                        .setTitle(resources.getString(R.string.typeOfItemToAdd))
                        .setItems(arrayOf(resources.getString(R.string.text), resources.getString(R.string.dateTime)), { _, position ->
                            val item =
                                if (position == 0) Item("", "text", "", "", false, false)
                                else Item(resources.getString(R.string.item), "date", "yyyy-MM-dd", "", false, false)
                            forms!![0].items?.add(item)
                            editFormAD?.notifyItemInserted(forms!![0].items!!.size - 1)
                            editFormAD?.setNameDialog(forms!![0].items.size - 1)
                        }).show()
            }
            false
        }
        editFormSaveBtn = findViewById(R.id.editFormSaveBtn)
        editFormSaveBtn?.setOnTouchListener { view, event ->
            editFormSaveBtn?.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                saveFormInNewTitle()
            }
            false
        }

        editFormRV = findViewById(R.id.editFormRV)
        editFormRV?.setHasFixedSize(true)
        editFormLM = LinearLayoutManager(this)

        applyForm()

        editFormRV?.layoutManager = editFormLM

        folderNameTV = findViewById(R.id.editFormFolderNameTV)
        folderNameBtn = findViewById(R.id.editFormFolderName)
        folderNameBtn?.setOnTouchListener { view, event ->
            folderNameBtn?.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                setFolderOrFileName(true)
            }
            false
        }
        fileNameTV = findViewById(R.id.editFormFileNameTV)
        fileNameBtn = findViewById(R.id.editFormFileName)
        fileNameBtn?.setOnTouchListener { view, event ->
            fileNameBtn?.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                setFolderOrFileName(false)
            }
            false
        }

        setFolderFileNameResult()
    }

    fun applyForm() {
        editFormAD = EditFormRCAdapter(this)
        editFormRV?.adapter = editFormAD
    }

    fun setFolderOrFileName(isFolder: Boolean) {
        var itemNames = Array<String>(forms!![0].items.size, { i ->
            forms!![0].items[i].name
        })

        AlertDialog.Builder(this)
                .setTitle("${if (isFolder) resources.getString(R.string.folder) else resources.getString(R.string.file)}${resources.getString(R.string.itemToBeUsedFor)}")
                .setItems(
                        itemNames, { _, position ->
                    forms!![0].items.map {
                        if (isFolder)
                            it.folderName = forms!![0].items.indexOf(it) == position
                        else
                            it.fileName = forms!![0].items.indexOf(it) == position
                        setFolderFileNameResult()
                    }
                }).show()
    }

    // 폴더명이나 파일명이 없는 상황이 발생하지 않도록
    fun secureFolderFileName() {
        var hasFolderName = false
        var hasFileName = false
        forms!![0].items.map {
            if (it.folderName)
                hasFolderName = true
            if (it.fileName)
                hasFileName = true
        }
        if (!hasFolderName)
            forms!![0].items[0].folderName = true
        if (!hasFileName)
            forms!![0].items[0].fileName = true
        setFolderFileNameResult()
    }

    fun setFolderFileNameResult() {
        forms!![0].items?.map {
            if (it.folderName) {
                folderNameTV?.text = resources.getString(R.string.folderName) + it.name
            }
            if (it.fileName) {
                fileNameTV?.text = resources.getString(R.string.fileName) + it.name
            }
        }
    }
}