package ko.hyeonmin.boardpro.parts.Form.recyclerParts

import android.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.EditFormActivity
import ko.hyeonmin.boardpro.viewExtension.FormRCButton

/**
 * Created by junse on 2017-11-17.
 */
class EditFormRCAdapter(val activity: EditFormActivity): RecyclerView.Adapter<EditFormRCAdapter.ViewHolder>(), OnItemMoveListener {

    var touchHalper: ItemTouchHelper = ItemTouchHelper(MyTouchHelperCallback(this))
    init {
        touchHalper.attachToRecyclerView(activity.editFormRV)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val typeImg: ImageView = view.findViewById(R.id.editFormTypeImg)
        val nameTV: TextView = view.findViewById(R.id.editFormNameTV)
        val nameBtn: FormRCButton = view.findViewById(R.id.editFormNameBtn)
        val handle: FormRCButton = view.findViewById(R.id.editFormHandle)
        val deleteBtn: FormRCButton = view.findViewById(R.id.editFormDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? =
            ViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.edit_form_rc_viewholder, parent, false))

    override fun getItemCount(): Int = activity.forms!![0].items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.typeImg.setImageResource(if (activity.forms!![0].items[position].type == "text") R.drawable.cs_lb_rc_text else R.drawable.cs_lb_rc_date)
        holder.nameTV.text = activity.forms!![0].items[position].name
        holder.nameBtn.setOnTouchListener { view, event ->
            holder.nameBtn.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP)
                setNameDialog(position)
            false
        }
        holder.handle.setOnTouchListener { view, event ->
            holder.handle.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_DOWN) {
                touchHalper.startDrag(holder)
            }
            false
        }
        holder.deleteBtn.setOnTouchListener { view, event ->
            holder.deleteBtn.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                if (activity.forms!![0].items.size == 1) {
                    Toast.makeText(activity, activity.resources.getString(R.string.mustBeOneOrMoreItem), Toast.LENGTH_SHORT).show()
                } else {
                    activity.forms!![0].items.removeAt(position)
                    activity.secureFolderFileName()
                    activity.applyForm()
                }
            }
            false
        }
    }

    override fun onItemMove(from: Int, to: Int) {
        notifyItemMoved(from, to)
    }

    override fun onItemDropped(from: Int, to: Int) {
        activity.forms!![0].items.add(to, activity.forms!![0].items.removeAt(from))
        (0..activity.forms!![0].items.size).map {
            notifyItemChanged(it)
        }
        activity.saveForms()
    }

    fun setNameDialog(position: Int) {
        var nameEdit = EditText(activity)
        var dialogBuilder = AlertDialog.Builder(activity)
                .setTitle(activity.resources.getString(R.string.item))
                .setMessage(activity.resources.getString(R.string.emptyToMakeFullLine))
                .setCancelable(false)
                .setPositiveButton(activity.resources.getString(R.string.ok), { _, _ ->
                    activity.forms!![0].items[position].name = nameEdit.text.toString()
                    notifyDataSetChanged()
                })

        var dialog: AlertDialog = dialogBuilder.create()
        dialog.setView(nameEdit)
        dialog.show()
    }
}
