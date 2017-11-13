package ko.hyeonmin.boardpro.parts.Photo

import android.widget.TextView
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity

/**
 * Created by junse on 2017-11-13.
 */
class PhotoPanel(val activity: ConsoleActivity) {

    private val photoFolderText: TextView = activity.findViewById(R.id.photoFolderText)

    init {
    }

    fun setPhotoFolder(folderName: String) {
        photoFolderText.text = "/BoardPro/" + folderName
    }
}