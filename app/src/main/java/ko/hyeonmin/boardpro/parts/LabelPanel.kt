package ko.hyeonmin.boardpro.parts

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.TextView
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity
import ko.hyeonmin.boardpro.parts.recyclerParts.LabelRCAdapter
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by junse on 2017-11-06.
 */
class LabelPanel(val activity: ConsoleActivity) {

    var currentFormTitle: TextView? = null

    private var formArray: JSONArray = JSONArray(activity.caches?.formsJson)
    // 마지막으로 캐시된 형식이 없으면 리스트에서 첫번째 것을 가져온다.
    var selectedForm: JSONObject = if (activity.caches!!.lastUsedFormJson != "") JSONObject(activity.caches!!.lastUsedFormJson) else JSONObject(formArray[0].toString())

    private val labelRecyclerView: RecyclerView = activity.findViewById(R.id.labelRecyclerView)
    private val labelRCAdapter: LabelRCAdapter = LabelRCAdapter(activity)
    private val labelRCLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)

    init {
        currentFormTitle = activity.findViewById(R.id.currentFormTitle)
        currentFormTitle?.text = selectedForm["title"].toString()

        labelRecyclerView.setHasFixedSize(true)
        labelRecyclerView.layoutManager = labelRCLayoutManager
        labelRecyclerView.adapter = labelRCAdapter
    }
}