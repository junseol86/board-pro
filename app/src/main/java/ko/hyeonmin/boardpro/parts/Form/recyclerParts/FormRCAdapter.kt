package ko.hyeonmin.boardpro.parts.recyclerParts

import android.app.AlertDialog
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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

    var popupSelDateView: ConstraintLayout = activity.layoutInflater.inflate(R.layout.popup_select_date, null) as ConstraintLayout
    var calendarView: CalendarView = popupSelDateView.findViewById(R.id.popupSelDateCalendar)
    var dateFormBtn: FormRCButton = popupSelDateView.findViewById(R.id.popupSelDateFormDate)
    var dateFormTV: TextView = popupSelDateView.findViewById(R.id.popupSelDateFormDateTV)
    var timeFormBtn: FormRCButton = popupSelDateView.findViewById(R.id.popupSelDateFormTime)
    var timeFormTV: TextView = popupSelDateView.findViewById(R.id.popupSelDateFormTimeTV)
    var builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            .setView(popupSelDateView)
            .setPositiveButton("확인", { _, _ ->
            })
    var selectDialog: AlertDialog = builder.create()

    init{
        calendarView.setOnDateChangeListener { view, year, month, day ->
            var cal =  Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val min = cal.get(Calendar.MINUTE)
            cal.set(year, month, day, hour, min)
            setDateTimeButtonText(selectedPosition, cal.timeInMillis)
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
                    SimpleDateFormat(item.dateForm).format(Date())
                })
        holder?.contentEt?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (item.type == "text")
                    activity.forms!![0].items[position].content = p0!!.toString()
                activity.formPanel?.setFileFolderNameResult()
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
                    selectedPosition = position
                    selectDialog.show()
                    setDateTimeButtonText(position, calendarView.date)
                }
            }
            false
        }
    }

    fun setDateTimeButtonText(position: Int, date: Long) {
        Log.d("HOHOHOHO", calendarView.date.toString())
        val dateFormStrings = activity.forms!![0].items[position].dateForm.split(" ")
        dateFormTV.text = SimpleDateFormat(dateFormStrings[0]).format(Date(date))
        timeFormTV.text = if (dateFormStrings.size > 1) SimpleDateFormat(dateFormStrings[1]).format(Date(date)) else "(없음)"
    }

    fun setDateFormatDialog() {
        AlertDialog.Builder(activity)
                .setTitle("날짜 형식")
                .setItems(
                        Array(activity.resources.getStringArray(R.array.day_format).size, {i ->
                        SimpleDateFormat(activity.resources.getStringArray(R.array.day_format)[i]).format(Date(calendarView.date))}),
                       { _, _ ->

                })
                .show()
    }

    fun setTimeFormatDialog() {
        AlertDialog.Builder(activity)
                .setTitle("시간 형식")
                .setItems(
                        Array(activity.resources.getStringArray(R.array.time_format).size, {i ->
                            if (i == 0) "(없음)" else
                            SimpleDateFormat(activity.resources.getStringArray(R.array.time_format)[i]).format(Date(calendarView.date))}),
                        { _, _ ->

                        })
                .show()
    }

    override fun getItemCount(): Int = activity.forms!![0].items.size

}