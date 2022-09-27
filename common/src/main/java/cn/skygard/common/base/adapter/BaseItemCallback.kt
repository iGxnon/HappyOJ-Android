package cn.skygard.common.base.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 * kim.bifrost.lib_common.base.adapter.BaseItemCallback
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 10:20
 */
open class BaseItemCallback<T : Any>(
    val func: (T, T) -> Boolean
) : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return func(oldItem, newItem)
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    /**
     * 默认实现这个方法有一定的好处，避免每次刷新时都与缓存互换
     */
    override fun getChangePayload(oldItem: T, newItem: T): Any {
        return "123" // 这里只要不传入 null 就可以了
    }
}