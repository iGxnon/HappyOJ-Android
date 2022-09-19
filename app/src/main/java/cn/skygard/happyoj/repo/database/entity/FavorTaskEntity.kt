package cn.skygard.happyoj.repo.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "t_favor_task")
data class FavorTaskEntity(
    @PrimaryKey
    val tid: Int,  // 来自后端
    val title: String,
    val shortcut: String,
    val imageUrl: String = "",
    val mdUrl: String,
    val date: Date
)