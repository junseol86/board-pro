package ko.hyeonmin.boardpro.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import ko.hyeonmin.boardpro.models.Form
import ko.hyeonmin.boardpro.models.Item
import ko.hyeonmin.boardpro.models.ItemContent
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

    var itemContentsJson: String
        get() = formsSF.getString("itemContentsJSON", "")
        set(value) {
            formsSF.edit().putString("itemContentsJSON", value).commit()
        }

    init {
//        formsJson = ""
        if (formsJson == "") {

            var itemList = ArrayList<Item>()
            itemList.add(Item("공사명", "text", "", "보드프로사옥\n기초공사", true, false))
            itemList.add(Item("공종", "text", "", "토공", false, true))
            itemList.add(Item("위치", "text", "", "위치 입력", false, false))
            itemList.add(Item("상세위치", "text", "", "상세위치 입력", false, false))
            itemList.add(Item("내용", "text", "", "내용 입력", false, false))
            itemList.add(Item("날짜", "date", "yyyy-MM-dd", "", false, false))

            var form1 = Form("기본 보드", "2017-11-11", itemList)
            var formList = ArrayList<Form>()
            formList.add(form1)
            formsJson = Gson().toJson(formList)
        }

//        itemContentsJson = ""
        if (itemContentsJson == "") {
            var contentList = ArrayList<String>()
            contentList.add("토공")
            contentList.add("토공사")

            var itemContent = ItemContent("공종", contentList)
            var itemContentList = ArrayList<ItemContent>()
            itemContentList.add(itemContent)
            itemContentsJson = Gson().toJson(itemContentList)
        }

    }
}