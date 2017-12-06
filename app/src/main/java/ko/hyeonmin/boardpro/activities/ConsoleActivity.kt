package ko.hyeonmin.boardpro.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Window
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.parts.Form.FormPanel
import ko.hyeonmin.boardpro.parts.Photo.PhotoPanel
import ko.hyeonmin.boardpro.parts.activityExtension.FormSavingActivity
import ko.hyeonmin.boardpro.utils.Caches
import ko.hyeonmin.boardpro.utils.RequestCode

class ConsoleActivity : FormSavingActivity() {

    var formPanel: FormPanel? = null
    var photoPanel: PhotoPanel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_console)
        getPermissions()
        caches = Caches(this)
        formPanel = FormPanel(this)
        photoPanel = PhotoPanel(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RequestCode.EDIT_FORM -> formPanel?.editFormResult()
        }
    }

    override fun applyNewForm() {
        formPanel?.applyForm()
    }

    fun getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    ) {
                requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.INTERNET, android.Manifest.permission.CAMERA), 0)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults!![0] != PackageManager.PERMISSION_GRANTED) {
            finishAndRemoveTask()
            return
        }
        if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            finishAndRemoveTask()
            return
        }
        if (grantResults[2] != PackageManager.PERMISSION_GRANTED) {
            finishAndRemoveTask()
            return
        }
    }
}
