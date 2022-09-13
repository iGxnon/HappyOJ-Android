package cn.skygard.happyoj.domain.model

import java.util.*

data class TasksItem(
    val taskId: Int = -1,
    val title: String,
    val shortcut: String,
    val imageUrl: String = "",
    val mdUrl: String,
    val date: Date
)