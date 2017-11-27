package ko.hyeonmin.boardpro.parts.Form.recyclerParts

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity
import ko.hyeonmin.boardpro.models.ItemContent
import ko.hyeonmin.boardpro.viewExtension.FormRCButton
import java.util.*

/**
 * Created by junse on 2017-11-20.
 */
class SelectContentRCAdapter(val activity: ConsoleActivity, val givenPosition: Int, val itemContent: ItemContent): RecyclerView.Adapter<SelectContentRCAdapter.ViewHolder>(), OnItemMoveListener {

    var touchHalper: ItemTouchHelper = ItemTouchHelper(MyTouchHelperCallback(this))
    init {
        touchHalper.attachToRecyclerView(activity.formPanel!!.selectContentRV)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val handle: FormRCButton = view.findViewById(R.id.selectContentHandle)
        val deleteBtn: FormRCButton = view.findViewById(R.id.selectContentDelete)
        val titleTV: TextView = view.findViewById(R.id.selectContentTitle)
        val selector: FormRCButton = view.findViewById(R.id.selectContentSelector)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.handle?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN)
                touchHalper.startDrag(holder)
            false
        }
        holder?.deleteBtn?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (itemContent.contentList.size == 1) {
                    activity.formPanel?.adSelectContentBuilder?.dismiss()
                    saveItemContent()
                }
                itemContent.contentList.removeAt(position)
                notifyDataSetChanged()
            }
            false
        }
        holder?.titleTV?.text = itemContent.contentList[position]
        holder?.selector?.setOnTouchListener { view, event ->
            holder?.selector?.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                activity.formPanel?.adSelectContentBuilder!!.dismiss()
                Collections.swap(itemContent.contentList, 0, position)
                activity.forms!![0].items[givenPosition].content = itemContent.contentList[0]
                activity.formPanel?.formRCAdapter?.notifyItemChanged(givenPosition)
                saveItemContent()
            }
            false
        }
    }

    override fun getItemCount(): Int = itemContent.contentList.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? =
            ViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.select_content_rc_viewholder, parent, false))


    override fun onItemMove(from: Int, to: Int) {
        notifyItemMoved(from, to)
    }

    override fun onItemDropped(from: Int, to: Int) {
        itemContent.contentList.add(to, itemContent.contentList.removeAt(from))
        notifyDataSetChanged()
        saveItemContent()
    }

    fun saveItemContent() {
        activity.formPanel?.itemContents?.map {
            if (it.itemName == itemContent.itemName) {
                it.contentList = itemContent.contentList
            }
        }
        activity.caches?.itemContentsJson = Gson().toJson(activity.formPanel!!.itemContents)
    }
}