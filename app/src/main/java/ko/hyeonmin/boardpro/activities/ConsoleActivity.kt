package ko.hyeonmin.boardpro.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.parts.Form.FormPanel
import ko.hyeonmin.boardpro.parts.Photo.PhotoPanel
import ko.hyeonmin.boardpro.parts.activityExtension.FormSavingActivity
import ko.hyeonmin.boardpro.utils.Caches
import ko.hyeonmin.boardpro.utils.RequestCode

class ConsoleActivity : FormSavingActivity() {

    var caches: Caches? = null

    var photoPanel: PhotoPanel? = null
    var formPanel: FormPanel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_console)
        caches = Caches(this)
        formPanel = FormPanel(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RequestCode.EDIT_FORM -> formPanel?.editFormResult()
        }
    }
}
