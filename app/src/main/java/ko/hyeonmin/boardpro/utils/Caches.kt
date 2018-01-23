package ko.hyeonmin.boardpro.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Size
import com.google.gson.Gson
import ko.hyeonmin.boardpro.R
import ko.hyeonmin.boardpro.models.*
import ko.hyeonmin.boardpro.parts.Camera.enums.CmrRatio

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

    var boardSettingJson: String
        get() = formsSF.getString("boardSettingJSON", "")
        set(value) {
            formsSF.edit().putString("boardSettingJSON", value).commit()
        }

    var boardSizePosJson: String
        get() = formsSF.getString("boardSizePosJSON", "")
        set(value) {
            formsSF.edit().putString("boardSizePosJSON", value).commit()
        }

    var cameraSettingJson: String
        get() = formsSF.getString("cameraSettingJSON", "")
        set(value) {
            formsSF.edit().putString("cameraSettingJSON", value).commit()
        }

    init {
//        formsJson = ""
        if (formsJson == "") {

            val itemList = ArrayList<Item>()
            itemList.add(Item(activity.resources.getString(R.string.item1), "text", "", activity.resources.getString(R.string.content1), true, false))
            itemList.add(Item(activity.resources.getString(R.string.item2), "text", "", activity.resources.getString(R.string.content2), false, true))
            itemList.add(Item(activity.resources.getString(R.string.item3), "text", "", activity.resources.getString(R.string.content3), false, false))
            itemList.add(Item(activity.resources.getString(R.string.item4), "text", "", activity.resources.getString(R.string.content4), false, false))
            itemList.add(Item(activity.resources.getString(R.string.item5), "text", "", activity.resources.getString(R.string.content5), false, false))
            itemList.add(Item(activity.resources.getString(R.string.item6), "date", "yyyy-MM-dd", "", false, false))

            val form1 = Form("기본 보드", itemList)
            val formList = ArrayList<Form>()
            formList.add(form1)
            formsJson = Gson().toJson(formList)
        }

//        itemContentsJson = ""
        if (itemContentsJson == "") {
            var contentList = ArrayList<String>()
            contentList.add(activity.resources.getString(R.string.content2))
            contentList.add(activity.resources.getString(R.string.content2_2))

            val itemContent = ItemContent(activity.resources.getString(R.string.item2), contentList)
            val itemContentList = ArrayList<ItemContent>()
            itemContentList.add(itemContent)
            itemContentsJson = Gson().toJson(itemContentList)
        }

//        boardSettingJson = ""
        if (boardSettingJson == "") {
            val bs = BoardSetting("000000", "FFFFFF", "6fa8dc", 80)
            boardSettingJson = Gson().toJson(bs)
        }

        if (boardSizePosJson == "") {
            val bp = BoardSizePos(24f, 1f, 0f, 0f, 1f)
            boardSizePosJson = Gson().toJson(bp)
        }

//        cameraSettingJson = ""
        if (cameraSettingJson == "") {
            val cs = CameraSetting(CmrRatio._4_3, -1)
            cameraSettingJson = Gson().toJson(cs)
        }
    }
}