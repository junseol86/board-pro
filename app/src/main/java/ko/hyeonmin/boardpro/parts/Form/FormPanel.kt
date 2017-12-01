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

    // 첫 화면 상단의 보드 작성 리사이클러뷰
    private val labelRecyclerView: RecyclerView = activity.findViewById(R.id.formRecyclerView)
    var formRCAdapter: FormRCAdapter? = null
    private val labelRCLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)

    // 서식선택, 다른이름저장, 서식수정 버튼
    private val selectFormBtn: WhiteButton = activity.findViewById(R.id.selectForm)
    private val saveFormBtn: WhiteButton = activity.findViewById(R.id.saveForm)
    private val editFormBtn: WhiteButton = activity.findViewById(R.id.editForm)

    // 서식선택 다이얼로그와 리사이클러뷰
    var selectFormRV: RecyclerView? = null
    var adSelectFormBuilder: AlertDialog? = null

    // 항목 컨텐츠 선택 다이얼로그와 리사이클러뷰
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

    // 서식 내용 화면에 적용
    fun applyForm() {
        activity.forms = Gson().fromJson(activity.caches?.formsJson, object : TypeToken<ArrayList<Form>>() {}.type)

        formRCAdapter = FormRCAdapter(activity)
        labelRecyclerView.adapter = formRCAdapter
        setInterfaceText()
        activity.photoPanel?.previewCanvas?.invalidate()
    }

    // 인터페이스 상 텍스트 설정
    fun setInterfaceText() {
        currentFormTitle.text = activity.forms!![0].title
    }

    // 서식 선택 다이얼로그 열기
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

    // 항목 내용 선택 다이얼로그 열기
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

    // 서식 수정 화면으로 이동
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