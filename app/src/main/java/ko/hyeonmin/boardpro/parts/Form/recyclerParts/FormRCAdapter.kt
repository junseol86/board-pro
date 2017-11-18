package ko.hyeonmin.boardpro.parts.recyclerParts

import android.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity
import ko.hyeonmin.boardpro.viewExtension.FormRCButton
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by junse on 2017-11-06.
 */
class FormRCAdapter(val activity: ConsoleActivity): RecyclerView.Adapter<FormRCAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nameEt: EditText = view.findViewById(R.id.form_rc_name_et)
        val contentEt: EditText = view.findViewById(R.id.form_rc_content_et)
        val selectBtn: FormRCButton = view.findViewById(R.id.form_rc_select)
        val selectBtnImg: ImageView = view.findViewById(R.id.form_rc_select_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        return ViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.console_form_rc_viewholder, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item = activity.formPanel!!.forms!![0].items[position]
        holder?.nameEt?.setText(item.name)
        holder?.nameEt?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (item.type == "text")
                    activity.formPanel!!.forms!![0].items[position].name = p0!!.toString()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        holder?.contentEt?.setText(
                if (item.type == "text")
                    item.content
                else {
                    SimpleDateFormat(item.dateForm).format(Date())
                })
        holder?.contentEt?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (item.type == "text")
                    activity.formPanel!!.forms!![0].items[position].content = p0!!.toString()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        holder?.selectBtnImg?.setImageResource(if (item.type == "text") R.drawable.cs_lb_rc_select else R.drawable.cs_lb_rc_date)
        holder?.selectBtn?.setOnTouchListener { view, event ->
            holder?.selectBtn?.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                if (item.type == "text") {
                    activity.formPanel?.itemContents?.map {
                        if (it.itemName == item.name) {
                            var contents = Array(it.contentList.size, {i -> it.contentList[i]})
                            AlertDialog.Builder(activity)
                                    .setTitle(item.name + " 선택")
                                    .setItems(contents, { _, contentPosition ->
                                        item.content = contents[contentPosition]
                                        notifyItemChanged(position)
                                    })
                                    .show()
                        }
                    }
                } else {

                }
            }
            false
        }
    }

    override fun getItemCount(): Int {
        return activity.formPanel!!.forms!![0].items.size
    }

}