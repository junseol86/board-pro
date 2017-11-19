package ko.hyeonmin.boardpro.parts.Form.recyclerParts

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.EditFormActivity
import ko.hyeonmin.boardpro.viewExtension.FormRCButton
import java.util.*

/**
 * Created by junse on 2017-11-17.
 */
class EditFormRCAdapter(val activity: EditFormActivity): RecyclerView.Adapter<EditFormRCAdapter.ViewHolder>() {

    var itemFrom = 0
    var itemTo = 0

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val typeImg: ImageView = view.findViewById(R.id.editFormTypeImg)
        val nameTV: EditText = view.findViewById(R.id.editFormName)
        val handle: FormRCButton = view.findViewById(R.id.editFormHandle)
        val deleteBtn: FormRCButton = view.findViewById(R.id.editFormDelete)
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
                if (activity.forms!![0].items.size <= position) return
                activity.forms!![0].items[position].name = editable!!.toString()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (activity.forms!![0].items.size <= position) return
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (activity.forms!![0].items.size <= position) return
            }
        })
        holder.handle.setOnTouchListener { view, event ->
            holder.handle.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_DOWN)
                activity.editFormTH?.startDrag(holder)
            false
        }
        holder.deleteBtn.setOnTouchListener { view, event ->
            holder.deleteBtn.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                if (activity.forms!![0].items.size == 1) {
                    Toast.makeText(activity, "항목이 하나 이상 있어야 합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    activity.forms!![0].items.removeAt(position)
                    activity.secureFolderFileName()
                    activity.applyForm()
                }
            }
            false
        }
    }

    fun onItemMove(from: Int, to: Int) {
        Collections.swap(activity.forms!![0].items, from, to)
        notifyItemMoved(from, to)
    }

}
