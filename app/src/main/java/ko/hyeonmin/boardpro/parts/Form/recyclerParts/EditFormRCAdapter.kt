package ko.hyeonmin.boardpro.parts.Form.recyclerParts

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.EditFormActivity

/**
 * Created by junse on 2017-11-17.
 */
class EditFormRCAdapter(val activity: EditFormActivity): RecyclerView.Adapter<EditFormRCAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val typeImg: ImageView = view.findViewById(R.id.editFormTypeImg)
        val nameTV: EditText = view.findViewById(R.id.editFormName)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        return ViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.edit_form_rc_viewholder, parent, false))
    }

    override fun getItemCount(): Int {
        return activity.forms!![0].items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.typeImg.setImageResource(if (activity.forms!![0].items[position].type == "text") R.drawable.cs_lb_rc_text else R.drawable.cs_lb_rc_date)
        holder.nameTV.setText(activity.forms!![0].items[position].name)
        holder.nameTV.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                activity.forms!![0].items[position].name = editable!!.toString()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

}
