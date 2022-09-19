package cn.skygard.happyoj.repo.remote.service

import cn.skygard.happyoj.repo.remote.model.Result
import cn.skygard.happyoj.repo.remote.model.User
import retrofit2.http.*
import java.io.File

interface UserService {

    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String
    ): Result<Any>

    @GET("user/register/confirm")
    suspend fun registerConfirm(
        @Query("code") code: String
    ): Result<Any>

    @POST("user/password")
    suspend fun resetPassword(
        @Field("email") email: String
    ): Result<Any>

    @GET("user/password/confirm")
    suspend fun resetPasswordConfirm(
        @Query("code") code: String,
        @Query("password") password: String
    ): Result<Any>

    @GET("user/{uid}")
    suspend fun getUserData(
        @Path("uid") uid: Long
    ): Result<User>

    @GET("user/avatar")
    suspend fun getAvatar(
        @Query("picture_name") pictureName: String
    ): Result<Any>

    @POST("user/login/password")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Result<User.UserToken>

    @POST("user/avatar")
    suspend fun setAvatar(
        @Field("file") imgFile: File
    ): Result<Any>

    @GET("user/bind/github")
    suspend fun bindGithub(
        @Query("code") code: String
    ): Result<Any>

}