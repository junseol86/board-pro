package ko.hyeonmin.boardpro.parts.recyclerParts

import android.app.AlertDialog
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity
import ko.hyeonmin.boardpro.viewExtension.FormRCButton
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by junse on 2017-11-06.
 */
class FormRCAdapter(val activity: ConsoleActivity): RecyclerView.Adapter<FormRCAdapter.ViewHolder>() {

    var selectedPosition = 0
    var selectedTime = 0L

    var popupSelDateView: ConstraintLayout = activity.layoutInflater.inflate(R.layout.popup_select_date, null) as ConstraintLayout
    var calendarView: CalendarView = popupSelDateView.findViewById(R.id.popupSelDateCalendar)
    var dateFormBtn: FormRCButton = popupSelDateView.findViewById(R.id.popupSelDateFormDate)
    var dateFormTV: TextView = popupSelDateView.findViewById(R.id.popupSelDateFormDateTV)
    var timeFormBtn: FormRCButton = popupSelDateView.findViewById(R.id.popupSelDateFormTime)
    var timeFormTV: TextView = popupSelDateView.findViewById(R.id.popupSelDateFormTimeTV)
    var builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            .setView(popupSelDateView)
            .setPositiveButton(activity.resources.getString(R.string.ok), { _, _ ->
                notifyItemChanged(selectedPosition)
            })
    var selectDialog: AlertDialog = builder.create()

    init{
        calendarView.setOnDateChangeListener { _, year, month, day ->
            var cal =  Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val min = cal.get(Calendar.MINUTE)
            cal.set(year, month, day, hour, min)
            selectedTime = cal.timeInMillis
            setDateTimeButtonText(selectedPosition)
        }
        dateFormBtn.setOnTouchListener { view, event ->
            dateFormBtn.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP)
                setDateFormatDialog()
            false
        }
        timeFormBtn.setOnTouchListener { view, event ->
            timeFormBtn.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP)
                setTimeFormatDialog()
            false
        }
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nameEt: EditText = view.findViewById(R.id.form_rc_name_et)
        val contentEt: EditText = view.findViewById(R.id.form_rc_content_et)
        val selectBtn: FormRCButton = view.findViewById(R.id.form_rc_select)
        val selectBtnImg: ImageView = view.findViewById(R.id.form_rc_select_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? =
            ViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.console_form_rc_viewholder, parent, false))

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item = activity.forms!![0].items[position]
        holder?.nameEt?.setText(item.name)
        holder?.nameEt?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (item.type == "text")
                    activity.forms!![0].items[position].name = p0!!.toString()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        holder?.contentEt?.setText(
                if (item.type == "text")
                    item.content
                else {
                    SimpleDateFormat(item.dateForm).format(if (selectedTime == 0L) Date() else Date(selectedTime))
                })
        holder?.contentEt?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (item.type == "text")
                    activity.forms!![0].items[position].content = p0!!.toString()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        holder?.selectBtnImg?.setImageResource(if (item.type == "text") R.drawable.cs_lb_rc_select else R.drawable.cs_lb_rc_date)
        holder?.selectBtn?.setOnTouchListener { view, event ->
            holder?.selectBtn?.onTouch(view, event)
            if (event.action == MotionEvent.ACTION_UP) {
                if (item.type == "text") {
                    var hasSaved = false
                    activity.formPanel?.itemContents?.map {
                        if (it.itemName == item.name) {
                            hasSaved = true
                            activity.formPanel?.selectContentStart(position, it)
                        }
                    }
                    if (!hasSaved)
                        Toast.makeText(activity, activity.resources.getString(R.string.noContentSavedForThisItem), Toast.LENGTH_SHORT).show()
                } else {
                    selectedPosition = position
                    selectDialog.show()
                    setDateTimeButtonText(position)
                }
            }
            false
        }
    }

    fun setDateTimeButtonText(position: Int) {
        val dateFormStrings = activity.forms!![0].items[position].dateForm.split(" ")
        dateFormTV.text = SimpleDateFormat(dateFormStrings[0]).format(Date(if (selectedTime == 0L) calendarView.date else selectedTime))
        timeFormTV.text = if (dateFormStrings.size > 1) SimpleDateFormat(dateFormStrings[1]).format(Date(if (selectedTime == 0L) calendarView.date else selectedTime)) else "(없음)"
    }

    fun setDateFormatDialog() {
        AlertDialog.Builder(activity)
                .setTitle("날짜 형식")
                .setItems(
                        Array(activity.resources.getStringArray(R.array.day_format).size, {i ->
                        SimpleDateFormat(activity.resources.getStringArray(R.array.day_format)[i]).format(Date(if (selectedTime == 0L) calendarView.date else selectedTime))}),
                       { _, i ->
                           val beforeSelected = activity.forms!![0].items[selectedPosition].dateForm.split(" ")
                           val selected = activity.resources.getStringArray(R.array.day_format)[i] + (if (beforeSelected.size > 1) " ${activity.resources.getStringArray(R.array.time_format)[i]}" else "")
                           activity.forms!![0].items[selectedPosition].dateForm = selected
                           setDateTimeButtonText(selectedPosition)
                        })
                .show()
    }

    fun setTimeFormatDialog() {
        AlertDialog.Builder(activity)
                .setTitle(activity.resources.getString(R.string.timeFormat))
                .setItems(
                        Array(activity.resources.getStringArray(R.array.time_format).size, {i ->
                            if (i == 0) activity.resources.getString(R.string.none) else
                            SimpleDateFormat(activity.resources.getStringArray(R.array.time_format)[i]).format(Date(if (selectedTime == 0L) calendarView.date else selectedTime))}),
                        { _, i ->
                            val beforeSelected = activity.forms!![0].items[selectedPosition].dateForm.split(" ")
                            val selected = beforeSelected[0] + if (i == 0) "" else " ${activity.resources.getStringArray(R.array.time_format)[i]}"
                            activity.forms!![0].items[selectedPosition].dateForm = selected
                            setDateTimeButtonText(selectedPosition)
                        })
                .show()
    }

    override fun getItemCount(): Int = activity.forms!![0].items.size

}