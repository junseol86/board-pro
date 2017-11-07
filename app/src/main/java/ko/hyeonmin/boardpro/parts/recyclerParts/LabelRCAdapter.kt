package ko.hyeonmin.boardpro.parts.recyclerParts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by junse on 2017-11-06.
 */
class LabelRCAdapter(val activity: ConsoleActivity): RecyclerView.Adapter<LabelRCAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nameEt: EditText = view.findViewById(R.id.label_rc_name_et)
        val contentEt: EditText = view.findViewById(R.id.label_rc_content_et)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        return ViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.console_label_rc_viewholder, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val jo = JSONArray(activity.labelPanel!!.selectedForm["items"].toString())[position] as JSONObject
        holder?.nameEt?.setText(jo["name"].toString())
        holder?.contentEt?.setText(jo["content"].toString())
    }

    override fun getItemCount(): Int {
        return JSONArray(activity.labelPanel!!.selectedForm["items"].toString()).length()
    }

}