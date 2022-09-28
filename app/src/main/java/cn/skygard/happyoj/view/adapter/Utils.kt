package cn.skygard.happyoj.view.adapter

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

internal object Utils {

    fun getLastTime(date: Date?): String {
        if (date == null) return ""
        val subTime = System.currentTimeMillis() - date.time
        val MILLIS_LIMIT = 1000.0
        val SECONDS_LIMIT = 60 * MILLIS_LIMIT
        val MINUTES_LIMIT = 60 * SECONDS_LIMIT
        val HOURS_LIMIT = 24 * MINUTES_LIMIT
        val DAYS_LIMIT = 30 * HOURS_LIMIT
        return if (subTime < MINUTES_LIMIT) {
            (subTime / SECONDS_LIMIT).roundToInt()
                .toString() + " " + "分钟"
        } else if (subTime < HOURS_LIMIT) {
            (subTime / MINUTES_LIMIT).roundToInt().toString() + " " + "小时"
        } else if (subTime < DAYS_LIMIT) {
            (subTime / HOURS_LIMIT).roundToInt().toString() + " " + "天"
        } else date.asString()
    }

    fun getNewsTimeStr(date: Date): String {
        val subTime = System.currentTimeMillis() - date.time
        val MILLIS_LIMIT = 1000.0
        val SECONDS_LIMIT = 60 * MILLIS_LIMIT
        val MINUTES_LIMIT = 60 * SECONDS_LIMIT
        val HOURS_LIMIT = 24 * MINUTES_LIMIT
        val DAYS_LIMIT = 30 * HOURS_LIMIT
        return if (subTime < MILLIS_LIMIT) {
            "刚刚"
        } else if (subTime < SECONDS_LIMIT) {
            (subTime / MILLIS_LIMIT).roundToInt()
                .toString() + " " + "秒前"
        } else if (subTime < MINUTES_LIMIT) {
            (subTime / SECONDS_LIMIT).roundToInt()
                .toString() + " " + "分钟前"
        } else if (subTime < HOURS_LIMIT) {
            (subTime / MINUTES_LIMIT).roundToInt().toString() + " " + "小时前"
        } else if (subTime < DAYS_LIMIT) {
            (subTime / HOURS_LIMIT).roundToInt().toString() + " " + "天前"
        } else date.asString()
    }

    private fun Date.asString(format: String = "yyyy-MM-dd"): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(this)
    }

}