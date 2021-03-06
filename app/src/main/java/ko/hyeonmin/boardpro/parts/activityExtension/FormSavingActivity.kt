package ko.hyeonmin.boardpro.parts.activityExtension

import android.app.Activity
import android.app.AlertDialog
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import ko.hyeonmin.boardpro.R
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
                .setTitle(resources.getString(R.string.saveAsDifferentName))
                .setMessage(resources.getString(R.string.inputFormName))
                .setView(formTitleEt)
                .setPositiveButton(resources.getString(R.string.ok), { _, _ ->
                })
                .setNegativeButton(resources.getString(R.string.cancel), {_, _ ->})
        val ad = adBuilder.create()
        ad.show()
        ad.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val formTitle = formTitleEt.text.toString()
            if (formTitle.trim() == "") {
                Toast.makeText(this, resources.getString(R.string.inputFormName), Toast.LENGTH_SHORT).show()
            } else {
                var hasSameTitle = false
                forms?.map {
                    if (it.title == formTitle)
                        hasSameTitle = true
                }
                if (hasSameTitle)
                    Toast.makeText(this, resources.getString(R.string.existsSameName), Toast.LENGTH_SHORT).show()
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
        if (forms != null)
            caches?.formsJson = Gson().toJson(forms)
    }

    override fun onPause() {
        saveForms()
        super.onPause()
        overridePendingTransition(0, 0)
    }
}