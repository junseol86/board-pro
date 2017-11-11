package ko.hyeonmin.boardpro.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import ko.hyeonmin.boardpro.models.Form
import ko.hyeonmin.boardpro.models.Item
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by junse on 2017-11-05.
 */
class Caches(val activity: Activity) {

    private val formsSF: SharedPreferences = activity.getSharedPreferences("forms", Context.MODE_PRIVATE)


    var formsJson: String
        get() = formsSF.getString("formsJSON", "")
        set(value) {
            formsSF.edit().putString("formsJSON", value).commit()
        }

    var lastUsedFormJson: String
        get() = formsSF.getString("lastUsedFormJson", "")
        set(value) {
            formsSF.edit().putString("lastUsedFormJson", value).commit()
        }

    init {

        formsJson = ""

        if (formsJson == "") {

            var itemList = ArrayList<Item>()
            itemList.add(Item("공사명", "text", "공사명 입력", "", true, false))
            itemList.add(Item("공종", "text", "공종 입력", "", false, true))
            itemList.add(Item("위치", "text", "위치 입력", "", false, false))
            itemList.add(Item("내용", "text", "내용 입력", "", false, false))
            itemList.add(Item("날짜", "date", "", "yyyy-mm-dd", false, false))

            var form1 = Form("기본서식", "2017-11-11", itemList)
            var formsList = ArrayList<Form>()
            formsList.add(form1)
            formsJson = Gson().toJson(formsList)

        }

    }
}