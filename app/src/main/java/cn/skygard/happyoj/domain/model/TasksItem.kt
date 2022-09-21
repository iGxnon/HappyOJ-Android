package cn.skygard.happyoj.domain.model

import java.util.*

data class TasksItem(
    val taskId: Int = -1,
    val title: String,
    val summary: String,
    val imageUrl: String = "",
    val date: Date
)