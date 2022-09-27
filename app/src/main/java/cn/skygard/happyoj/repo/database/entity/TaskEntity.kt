package cn.skygard.happyoj.repo.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// 本地缓存的任务列表
@Entity(tableName = "t_tasks")
data class TaskEntity(
    @PrimaryKey
    val tid: Long,  // 来自后端
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "summary", defaultValue = "")
    val summary: String,
    @ColumnInfo(name = "image_url", defaultValue = "")
    val imageUrl: String,
    @ColumnInfo(name = "md_content", defaultValue = "")
    val mdContent: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "repo_url", defaultValue = "")
    val repoUrl: String,
    @ColumnInfo(name = "repo_type", defaultValue = "github")
    val repoType: String
)