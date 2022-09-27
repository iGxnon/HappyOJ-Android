package cn.skygard.happyoj.repo.database.dao

import androidx.room.*
import cn.skygard.happyoj.repo.database.entity.TaskEntity

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userEntity: TaskEntity)

    @Delete
    suspend fun delete(userEntity: TaskEntity)

    @Query("SELECT md_content FROM t_tasks WHERE tid = :tid")
    suspend fun getContent(tid: Long): String?

    @Query("SELECT repo_url FROM t_tasks WHERE tid = :tid")
    suspend fun getRepoUrl(tid: Long): String?

    @Query("SELECT repo_type FROM t_tasks WHERE tid = :tid")
    suspend fun getRepoType(tid: Long): String?

    @Query("UPDATE t_tasks SET repo_url = :url WHERE tid = :tid")
    suspend fun updateRepoUrl(tid: Long, url: String?)

}