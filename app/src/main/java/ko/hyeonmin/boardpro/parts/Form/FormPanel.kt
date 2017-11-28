package ko.hyeonmin.boardpro.parts.Form

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity
import ko.hyeonmin.boardpro.activities.EditFormActivity
import ko.hyeonmin.boardpro.models.Form
import ko.hyeonmin.boardpro.models.ItemContent
import ko.hyeonmin.boardpro.parts.Form.recyclerParts.SelectContentRCAdapter
import ko.hyeonmin.boardpro.parts.Form.recyclerParts.SelectFormRCAdapter
import ko.hyeonmin.boardpro.parts.Photo.PhotoPanel
import ko.hyeonmin.boardpro.parts.recyclerParts.FormRCAdapter
import ko.hyeonmin.boardpro.utils.RequestCode
import ko.hyeonmin.boardpro.viewExtension.WhiteButton

/**
 * Created by junse on 2017-11-06.
 */
class FormPanel(val activity: ConsoleActivity) {

    var currentFormTitle: TextView = activity.findViewById(R.id.currentFormTitle)

    var itemContents: ArrayList<ItemContent> = Gson().fromJson(activity.caches?.itemContentsJson, object : TypeToken<ArrayList<ItemContent>>() {}.type)

    private val labelRecyclerView: RecyclerView = activity.findViewById(R.id.formRecyclerView)
    var formRCAdapter: FormRCAdapter? = null
    private val labelRCLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)

    private val selectFormBtn: WhiteButton = activity.findViewById(R.id.selectForm)
    private val saveFormBtn: WhiteButton = activity.findViewById(R.id.saveForm)
    private val editFormBtn: WhiteButton = activity.findViewById(R.id.editForm)

    var selectFormRV: RecyclerView? = null
    var adSelectFormBuilder: AlertDialog? = null

    var selectContentRV: RecyclerView? = null
    var adSelectContentBuilder: AlertDialog? = null

    init {
        activity.photoPanel = PhotoPanel(activity)

        labelRecyclerView.setHasFixedSize(true)
        labelRecyclerView.layoutManager = labelRCLayoutManager

        applyForm()

        selectFormBtn.setOnTouchListener { view, event ->
            selectFormBtn.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP)
                selectFormStart()
            false
        }

        saveFormBtn.setOnTouchListener { view, event ->
            saveFormBtn.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP)
                activity.saveFormInNewTitle()
            false
        }

        editFormBtn.setOnTouchListener { view, event ->
            editFormBtn.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP)
                editFormStart()
            false
        }


    }

    fun applyForm() {
        activity.forms = Gson().fromJson(activity.caches?.formsJson, object : TypeToken<ArrayList<Form>>() {}.type)

        formRCAdapter = FormRCAdapter(activity)
        labelRecyclerView.adapter = formRCAdapter
        setInterfaceText()
    }

    fun setInterfaceText() {
        currentFormTitle.text = activity.forms!![0].title
    }

    fun selectFormStart() {
        selectFormRV = RecyclerView(activity)
        var selectFormLM = LinearLayoutManager(activity)
        selectFormRV?.layoutManager = selectFormLM

        var selectFormAD = SelectFormRCAdapter(activity)
        selectFormRV?.adapter = selectFormAD

        adSelectFormBuilder = AlertDialog.Builder(activity).create()
        adSelectFormBuilder?.setView(selectFormRV)
        adSelectFormBuilder?.show()
    }

    fun selectContentStart(givenPosition: Int, itemContent: ItemContent) {
        selectContentRV = RecyclerView(activity)
        var selectContentLM = LinearLayoutManager(activity)
        selectContentRV?.layoutManager = selectContentLM

        var selectContentAD = SelectContentRCAdapter(activity, givenPosition, itemContent)
        selectContentRV?.adapter = selectContentAD

        adSelectContentBuilder = AlertDialog.Builder(activity).create()
        adSelectContentBuilder?.setView(selectContentRV)
        adSelectContentBuilder?.show()
    }

    fun editFormStart() {
        activity.saveForms()
        var intent = Intent(activity, EditFormActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        activity.startActivityForResult(intent, RequestCode.EDIT_FORM)
    }

    fun editFormResult() {
        applyForm()
    }

}