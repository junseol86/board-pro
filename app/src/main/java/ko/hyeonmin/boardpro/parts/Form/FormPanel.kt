package ko.hyeonmin.boardpro.parts.Form

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity
import ko.hyeonmin.boardpro.activities.EditFormActivity
import ko.hyeonmin.boardpro.models.Form
import ko.hyeonmin.boardpro.models.ItemContent
import ko.hyeonmin.boardpro.parts.Photo.PhotoPanel
import ko.hyeonmin.boardpro.parts.recyclerParts.FormRCAdapter
import ko.hyeonmin.boardpro.utils.RequestCode
import ko.hyeonmin.boardpro.viewExtension.WhiteButton

/**
 * Created by junse on 2017-11-06.
 */
class FormPanel(val activity: ConsoleActivity) {

    var currentFormTitle: TextView = activity.findViewById(R.id.currentFormTitle)

    var forms: ArrayList<Form>? = null

    var itemContents: ArrayList<ItemContent> = Gson().fromJson(activity.caches?.itemContentsJson, object : TypeToken<ArrayList<ItemContent>>() {}.type)

    private val labelRecyclerView: RecyclerView = activity.findViewById(R.id.formRecyclerView)
    private var formRCAdapter: FormRCAdapter? = null
    private val labelRCLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)

    private val editFormBtn: WhiteButton = activity.findViewById(R.id.editForm)

    init {
        activity.photoPanel = PhotoPanel(activity)

        labelRecyclerView.setHasFixedSize(true)
        labelRecyclerView.layoutManager = labelRCLayoutManager

        applyForm()

        editFormBtn.setOnTouchListener { view, event ->
            editFormBtn.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                editFormStart()
            }
            false
        }


    }

    fun applyForm() {
        forms = Gson().fromJson(activity.caches?.formsJson, object : TypeToken<ArrayList<Form>>() {}.type)

        formRCAdapter = FormRCAdapter(activity)
        labelRecyclerView.adapter = formRCAdapter
        setInterfaceText()
    }

    fun setInterfaceText() {
        currentFormTitle.text = forms!![0].title
        setFileFolderNameResult()
    }

    fun setFileFolderNameResult() {
//        항목 foNm, fiNm 값에 따라 파일명, 폴더명을 표시
        forms!![0].items.map {
            if (it.folderName) {
                activity.photoPanel?.setPhotoFolder(it.name.replace(" ", "_").replace("\n", "_"))
            }
            if (it.fileName) {
            }
        }
    }

    // 현재 입력된 서식을 입시로 캐시화
    fun saveCurrentForm() {
        activity.caches?.formsJson = Gson().toJson(forms)
    }

    fun editFormStart() {
        saveCurrentForm()
        var intent = Intent(activity, EditFormActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        activity.startActivityForResult(intent, RequestCode.EDIT_FORM)
    }

    fun editFormResult() {
        applyForm()
    }

    fun saveToLastForm() {

    }

}