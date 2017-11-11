package ko.hyeonmin.boardpro.parts

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity
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
    var folderNameTV: TextView = activity.findViewById(R.id.folderNameTV)
    var fileNameTV: TextView = activity.findViewById(R.id.fileNameTV)

    private var formArray: JSONArray = JSONArray(activity.caches?.formsJson)
    // 마지막으로 캐시된 형식이 없으면 리스트에서 첫번째 것을 가져온다.
    var selectedForm: JSONObject = if (activity.caches!!.lastUsedFormJson != "") JSONObject(activity.caches!!.lastUsedFormJson) else JSONObject(formArray[0].toString())
    var tempForm: JSONObject? = null

    private val labelRecyclerView: RecyclerView = activity.findViewById(R.id.labelRecyclerView)
    private val labelRCAdapter: LabelRCAdapter = LabelRCAdapter(activity)
    private val labelRCLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)

    private val editFormBtn: WhiteButton = activity.findViewById(R.id.editForm)

    init {
        currentFormTitle.text = selectedForm["title"].toString()

        editFormBtn.setOnTouchListener { view, event ->
            editFormBtn.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                editingForm = !editingForm
                labelPanelBottom.visibility = if (editingForm) View.VISIBLE else View.GONE
                labelRCAdapter.notifyDataSetChanged()
            }
            false
        }

        labelRecyclerView.setHasFixedSize(true)
        labelRecyclerView.layoutManager = labelRCLayoutManager
        labelRecyclerView.adapter = labelRCAdapter

        setInterfaceText()
    }

    fun setInterfaceText() {
//        항목 foNm, fiNm 값에 따라 파일명, 폴더명을 표시
        for (i in 0 until JSONArray(selectedForm["items"].toString()).length()) {
//            var jo = JSONArray(selectedForm["items"].toString())[i] as JSONObject
//            if (Integer.parseInt(jo["folderName"].toString()) == 1) {
//                folderNameTV.text = "폴더명: " + jo["name"].toString()
//            }
//            if (Integer.parseInt(jo["fileName"].toString()) == 1) {
//                fileNameTV.text = "파일명: " + jo["name"].toString()
//            }
        }
    }

    fun saveToLastForm() {

    }

}