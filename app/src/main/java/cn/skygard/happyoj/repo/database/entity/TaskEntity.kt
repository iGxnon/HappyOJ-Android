package cn.skygard.happyoj.repo.database.entity

import androidx.room.*

// 本地缓存的任务列表
@Entity(tableName = "t_tasks")
@Fts4(tokenizer = FtsOptions.TOKENIZER_ICU) // 开启全文索引
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
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
)