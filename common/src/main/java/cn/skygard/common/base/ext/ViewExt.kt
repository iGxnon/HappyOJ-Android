@file:Suppress("UNUSED")
package cn.skygard.common.base.ext

import android.view.View

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/3/24 23:08
 */

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * @param interval 毫秒为单位，点击间隔小于这个值监听事件无法生效
 * @param click 具体的点击事件
 */
fun View.setOnSingleClickListener(interval: Long = 500, click: (View) -> Unit) {
    setOnClickListener {
        val tag = getTag(114514) as? Long  // 这么臭的 tag 应该没有人会用罢！
        if (System.currentTimeMillis() - (tag ?: 0L) > interval) {
            click(it)
        }
        it.setTag(114514, System.currentTimeMillis())
    }
}