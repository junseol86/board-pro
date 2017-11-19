package ko.hyeonmin.boardpro.parts.activityExtension

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import ko.hyeonmin.boardpro.models.Form

/**
 * Created by junse on 2017-11-19.
 */

open class FormSavingActivity: Activity() {

    var forms: ArrayList<Form>? = null

    fun saveFormInNewName() {
        val formNameEt = EditText(this)
        AlertDialog.Builder(this)
                .setTitle("보드 제목을 입력하세요.")
                .setView(formNameEt)
                .setPositiveButton("확인", { dialog, _ ->
                    val formName = formNameEt.text.toString()
                    if (formName.trim() == "") {
                        Toast.makeText(this, "제목을 입력하세요.", Toast.LENGTH_SHORT)
                    } else {
                        var hasSameName = false
                        forms?.map {
                            if (it.title == formName)
                                hasSameName = true
                            if (hasSameName)
                                Toast.makeText(this, "같은 제목의 보드가 있습니다.", Toast.LENGTH_SHORT)
                            else {
                                dialog.dismiss()
                            }
                        }
                    }
                })
                .setNegativeButton("취소", {_, _ ->})
                .show()
    }
}