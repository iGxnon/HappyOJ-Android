package cn.skygard.happyoj.repo.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// 本地缓存的任务列表
@Entity(tableName = "t_favor_tasks")
data class FavorTaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    val id: Int? = null, // 默认主键
    @ColumnInfo(name = "tid")
    val tid: Int,  // 来自后端
)