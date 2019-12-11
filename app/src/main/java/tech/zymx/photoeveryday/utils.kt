package tech.zymx.photoeveryday

import android.os.Build
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kevinzhan@tencent.com on 2019-12-11
 */

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