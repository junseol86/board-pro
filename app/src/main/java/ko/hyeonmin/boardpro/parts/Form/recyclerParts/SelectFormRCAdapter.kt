package ko.hyeonmin.boardpro.parts.Form.recyclerParts

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity
import ko.hyeonmin.boardpro.viewExtension.FormRCButton
import java.util.*

/**
 * Created by junse on 2017-11-20.
 */
class SelectFormRCAdapter(val activity: ConsoleActivity): RecyclerView.Adapter<SelectFormRCAdapter.ViewHolder>(), OnItemMoveListener {

    var touchHalper: ItemTouchHelper = ItemTouchHelper(MyTouchHelperCallback(this))
    init {
        touchHalper.attachToRecyclerView(activity.formPanel!!.selectFormRV)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val handle: FormRCButton = view.findViewById(R.id.selectFormHandle)
        val deleteBtn: FormRCButton = view.findViewById(R.id.selectFormDelete)
        val titleTV: TextView = view.findViewById(R.id.selectFormTitle)
        val selector: FormRCButton = view.findViewById(R.id.selectFormSelector)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.handle?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN)
                touchHalper.startDrag(holder)
            false
        }
        holder?.deleteBtn?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (activity.forms!!.size == 1) {
                    Toast.makeText(activity, activity.resources.getString(R.string.mustBeOneOrMoreForm), Toast.LENGTH_SHORT).show()
                } else {
                    activity.forms!!.removeAt(position)
                    notifyDataSetChanged()
                    activity.saveForms()
                    if (position == 0)
                        activity.applyNewForm()
                }
            }
            false
        }
        holder?.titleTV?.text = activity.forms!![position].title
        holder?.selector?.setOnTouchListener { view, event ->
            holder?.selector?.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                activity.formPanel?.adBuilder!!.dismiss()
                Collections.swap(activity.forms!!, 0, position)
                activity.saveForms()
                activity.formPanel?.applyForm()
            }
            false
        }
    }

    override fun getItemCount(): Int = activity.forms!!.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? =
            ViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.select_form_rc_viewholder, parent, false))


    override fun onItemMove(from: Int, to: Int) {
        notifyItemMoved(from, to)
    }

    override fun onItemDropped(from: Int, to: Int) {
        activity.forms!!.add(to, activity.forms!!.removeAt(from))
        notifyDataSetChanged()
        activity.saveForms()
        activity.formPanel?.applyForm()
    }
}