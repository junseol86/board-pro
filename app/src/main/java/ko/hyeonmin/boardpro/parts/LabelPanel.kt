package ko.hyeonmin.boardpro.parts

import android.support.v7.widget.RecyclerView
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.activities.ConsoleActivity

/**
 * Created by junse on 2017-11-06.
 */
class LabelPanel(val activity: ConsoleActivity) {

    val labelRecyclerView: RecyclerView = activity.findViewById(R.id.labelRecyclerView)
}