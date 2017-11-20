package ko.hyeonmin.boardpro.parts.activityExtension

import android.app.Activity
import android.app.AlertDialog
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import ko.hyeonmin.boardpro.models.Form
import ko.hyeonmin.boardpro.utils.Caches

/**
 * Created by junse on 2017-11-19.
 */

open class FormSavingActivity: Activity() {

    var caches: Caches? = null
    var forms: ArrayList<Form>? = null

    fun saveFormInNewTitle() {
        val formTitleEt = EditText(this)
        val adBuilder = AlertDialog.Builder(this)
        adBuilder
                .setTitle("서식의 제목을 입력하세요.")
                .setView(formTitleEt)
                .setPositiveButton("확인", { _, _ ->
                })
                .setNegativeButton("취소", {_, _ ->})
        val ad = adBuilder.create()
        ad.show()
        ad.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val formTitle = formTitleEt.text.toString()
            if (formTitle.trim() == "") {
                Toast.makeText(this, "제목을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                var hasSameTitle = false
                forms?.map {
                    if (it.title == formTitle)
                        hasSameTitle = true
                }
                if (hasSameTitle)
                    Toast.makeText(this, "같은 제목의 보드가 있습니다.", Toast.LENGTH_SHORT).show()
                else {
                    val gson = Gson()
                    val newFormJson = gson.toJson(forms!![0])
                    val newForm = gson.fromJson(newFormJson, Form::class.java)
                    newForm.title = formTitleEt.text.toString()
                    forms!!.add(0, newForm)
                    caches?.formsJson = Gson().toJson(forms)
                    ad.dismiss()
                    applyNewForm()
                }
            }
        }
    }

    open fun applyNewForm() {}

    open fun saveForms() {
        caches?.formsJson = Gson().toJson(forms)
    }

    override fun onPause() {
        saveForms()
        super.onPause()
        overridePendingTransition(0, 0)
    }
}