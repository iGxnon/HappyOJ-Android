package cn.skygard.happyoj.repo.remote.service

import cn.skygard.happyoj.repo.remote.model.*
import retrofit2.http.*

interface TaskService {

    @GET("task/list")
    suspend fun getTasks(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): Result<Tasks>

    @GET("task")
    suspend fun getTaskDetail(
        @Query("task_id") tid: Long
    ): Result<Task>

    @POST("task/submit")
    @FormUrlEncoded
    suspend fun submitRepo(
        @Field("task_id") tid: Long,
        @Field("repo_url") repoUrl: String,
        @Field("description") description: String
    ): Result<Any>

    @PATCH("task/commit")
    @FormUrlEncoded
    suspend fun patchRepo(
        @Field("commit_id") cid: Long,
        @Field("repo_url") repoUrl: String,
        @Field("description") description: String
    ): Result<Any>

    @GET("task/{tid}/user")
    suspend fun getTaskCommit(
        @Path("tid") tid: Long,
    ): Result<TaskCommit>

    @GET("task/commit/user")
    suspend fun getLoginUserCommits(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): Result<TaskCommits>

}