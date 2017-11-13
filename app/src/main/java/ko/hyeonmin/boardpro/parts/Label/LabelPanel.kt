package ko.hyeonmin.boardpro.parts.Label

import android.app.AlertDialog
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity
import ko.hyeonmin.boardpro.models.Form
import ko.hyeonmin.boardpro.models.ItemContent
import ko.hyeonmin.boardpro.parts.Photo.PhotoPanel
import ko.hyeonmin.boardpro.parts.recyclerParts.LabelRCAdapter
import ko.hyeonmin.boardpro.viewExtension.WhiteButton
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by junse on 2017-11-06.
 */
class LabelPanel(val activity: ConsoleActivity) {

    var editingForm: Boolean = false
    var currentFormTitle: TextView = activity.findViewById(R.id.currentFormTitle)

    var labelPanelBottom: ConstraintLayout = activity.findViewById(R.id.labelPanelBottom)

    var folderNameWb: WhiteButton = activity.findViewById(R.id.labelFolderName)
    var folderNameTV: TextView = activity.findViewById(R.id.folderNameTV)
    var fileNameWb: WhiteButton = activity.findViewById(R.id.labelFileName)
    var fileNameTV: TextView = activity.findViewById(R.id.fileNameTV)

    private var formArray: JSONArray = JSONArray(activity.caches?.formsJson)
    private var forms: ArrayList<Form> = Gson().fromJson(activity.caches?.formsJson, object : TypeToken<ArrayList<Form>>() {}.type)
    // 마지막으로 캐시된 형식이 없으면 리스트에서 첫번째 것을 가져온다.
    var selectedForm: Form = if (activity.caches!!.lastUsedFormJson != "") Gson().fromJson(activity.caches?.lastUsedFormJson, Form::class.java) else forms[0]
    var tempForm: JSONObject? = null

    var itemContents: ArrayList<ItemContent> = Gson().fromJson(activity.caches?.itemContentsJson, object : TypeToken<ArrayList<ItemContent>>() {}.type)

    private val labelRecyclerView: RecyclerView = activity.findViewById(R.id.labelRecyclerView)
    private val labelRCAdapter: LabelRCAdapter = LabelRCAdapter(activity)
    private val labelRCLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)

    private val editFormBtn: WhiteButton = activity.findViewById(R.id.editForm)

    init {
        activity.photoPanel = PhotoPanel(activity)
        currentFormTitle.text = selectedForm.title

        editFormBtn.setOnTouchListener { view, event ->
            editFormBtn.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                editingForm = !editingForm
                labelPanelBottom.visibility = if (editingForm) View.VISIBLE else View.GONE
                labelRCAdapter.notifyDataSetChanged()
            }
            false
        }

        folderNameWb.setOnTouchListener { view, event ->
            folderNameWb.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                setFolderFileName(true)
            }
            false
        }
        fileNameWb.setOnTouchListener { view, event ->
            fileNameWb.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                setFolderFileName(false)
            }
            false
        }

        labelRecyclerView.setHasFixedSize(true)
        labelRecyclerView.layoutManager = labelRCLayoutManager
        labelRecyclerView.adapter = labelRCAdapter

        setInterfaceText()
    }

    fun setInterfaceText() {
        setFileFolderNameResult()
    }

    fun setFolderFileName(folderOrFile: Boolean) {
        var items = Array(selectedForm.items.size, {i -> selectedForm.items[i].name})
        AlertDialog.Builder(activity)
                .setTitle(if (folderOrFile) "폴더명 선택" else "파일명 선택")
                .setItems(items, { _, position ->
                    selectedForm.items.map {
                        if (folderOrFile)
                            it.folderName = false
                        else
                            it.fileName = false
                    }
                    if (folderOrFile)
                        selectedForm.items[position].folderName = true
                    else
                        selectedForm.items[position].fileName = true
                    setFileFolderNameResult()
                })
                .show()
    }

    fun setFileFolderNameResult() {
//        항목 foNm, fiNm 값에 따라 파일명, 폴더명을 표시
        selectedForm.items.map {
            if (it.folderName) {
                folderNameTV.text = "폴더명: " + it.name
                activity.photoPanel?.setPhotoFolder(it.name.replace(" ", "_").replace("\n", "_"))
            }
            if (it.fileName) {
                fileNameTV.text = "파일명: " + it.name
            }
        }
    }



    fun saveToLastForm() {

    }

}