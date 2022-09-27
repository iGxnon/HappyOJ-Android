package cn.skygard.happyoj.repo.remote.service

import cn.skygard.happyoj.repo.remote.model.RepoBranch
import cn.skygard.happyoj.repo.remote.model.RepoCommit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RepoService {

    @GET("repos/{owner}/{repo}/commits")
    suspend fun getRepoCommits(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): List<RepoCommit>

    @GET("repos/{owner}/{repo}/branches")
    suspend fun getRepoBranches(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
    ): List<RepoBranch>

}