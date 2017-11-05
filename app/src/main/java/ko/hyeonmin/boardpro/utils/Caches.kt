package ko.hyeonmin.boardpro.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by junse on 2017-11-05.
 */
class Caches(val activity: Activity) {

    private val formsSF: SharedPreferences = activity.getSharedPreferences("forms", Context.MODE_PRIVATE)


    var formsJson: String
        get() = formsSF.getString("formsSF", "")
        set(value) {
            formsSF.edit().putString("formsSF", value).commit()
        }

    init {
        if (formsJson == "") {
            var formsArray = JSONArray()

            val form1 = JSONObject()
            form1.put("title", "기본서식")

            val itemsArray = JSONArray()

            val item1 = JSONObject()
            item1.put("name", "공사명")
            item1.put("type", "text")
            item1.put("content", "공사명 입력")
            item1.put("dateForm", "")

            val item2 = JSONObject()
            item2.put("name", "공종")
            item2.put("type", "text")
            item2.put("content", "공종 입력")
            item2.put("dateForm", "")

            val item3 = JSONObject()
            item3.put("name", "위치")
            item3.put("type", "text")
            item3.put("content", "위치 입력")
            item3.put("dateForm", "")

            val item4 = JSONObject()
            item4.put("name", "내용")
            item4.put("type", "text")
            item4.put("content", "내용 입력")
            item4.put("dateForm", "")

            val item5 = JSONObject()
            item5.put("name", "일자")
            item5.put("type", "date")
            item5.put("content", "")
            item5.put("dateForm", "yyyy.mm.dd")

            itemsArray.put(item1)
            itemsArray.put(item2)
            itemsArray.put(item3)
            itemsArray.put(item4)
            itemsArray.put(item5)

            form1.put("items", itemsArray)
            formsArray.put(form1)

            formsJson = formsArray.toString()
        }

    }
}