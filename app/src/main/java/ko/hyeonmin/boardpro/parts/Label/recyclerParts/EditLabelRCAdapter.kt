package ko.hyeonmin.boardpro.parts.Label.recyclerParts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.EditLabelActivity

/**
 * Created by junse on 2017-11-17.
 */
class EditLabelRCAdapter(val activity: EditLabelActivity): RecyclerView.Adapter<EditLabelRCAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val typeImg: ImageView = view.findViewById(R.id.editLabelTypeImg)
        val nameTV: EditText = view.findViewById(R.id.editLabelName)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        return ViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.edit_label_rc_viewholder, parent, false))
    }

    override fun getItemCount(): Int {
        return activity.form!!.items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = activity.form!!.items[position]
        holder.typeImg.setImageResource(if (item.type == "text") R.drawable.cs_lb_rc_text else R.drawable.cs_lb_rc_date)
        holder.nameTV.setText(item.name)
    }

}
