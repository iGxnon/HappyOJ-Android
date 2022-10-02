package cn.skygard.happyoj.repo.database.dao

import androidx.room.*
import cn.skygard.happyoj.repo.database.entity.TaskEntity

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userEntity: TaskEntity)

    @Delete
    suspend fun delete(userEntity: TaskEntity)

    @Query("SELECT rowid, title, summary, image_url, md_content, deadline, date FROM t_tasks WHERE rowid = :tid")
    suspend fun get(tid: Long): TaskEntity?

    @Query("SELECT md_content FROM t_tasks WHERE rowid = :tid")
    suspend fun getContent(tid: Long): String?

    @Query("SELECT rowid, title, summary, image_url, md_content, deadline, date FROM t_tasks WHERE md_content MATCH :content")
    suspend fun matchContent(content: String): List<TaskEntity>?

    @Query("SELECT rowid, title, summary, image_url, md_content, deadline, date FROM t_tasks WHERE title LIKE :title")
    suspend fun matchTitle(title: String): List<TaskEntity>?

    @Query("SELECT rowid, title, summary, image_url, md_content, deadline, date FROM t_tasks WHERE summary LIKE :summary")
    suspend fun matchSummary(summary: String): List<TaskEntity>?

}