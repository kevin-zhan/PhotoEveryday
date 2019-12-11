package tech.zymx.photoeveryday

import android.os.Build
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kevinzhan@tencent.com on 2019-12-11
 */


enum class UIActionType {
    UI_ACTION_NONE,
    UI_ACTION_ADD_PAGE,
    UI_ACTION_DETAIL_PAGE,
    UI_ACTION_PHOTO_LIBRARY,
    UI_ACTION_SHOW_TOAST,
    UI_ACTION_ADD_DONE,
    UI_ACTION_SHOW_DELETE_DIALOG,
}

const val REQUEST_CODE_ADD_PAGE = 10000
const val REQUEST_CODE_PHOTO_LIBRARY = 10001

const val PERMISSION_REQ_WRITE_EXT = 20000

fun formatTimestamp(timestamp: Long): String {
    Date(timestamp).let { date ->
        val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("yyyy-MM-dd HH:mm", MyApplication.g().resources.configuration.locales[0])
        } else {
            SimpleDateFormat("yyyy-MM-dd HH:mm", MyApplication.g().resources.configuration.locale)
        }
        return formatter.format(date)
    }
}