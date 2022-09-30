package cn.skygard.happyoj.repo.remote.service

import cn.skygard.happyoj.repo.remote.model.Login
import cn.skygard.happyoj.repo.remote.model.Result
import cn.skygard.happyoj.repo.remote.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*
import java.io.File

interface UserService {

    @POST("user/register/email")
    @FormUrlEncoded
    suspend fun registerMail(
        @Field("email") email: String
    ): Result<Any>

    @POST("user/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("stu_id") stuId: String,
        @Field("name") name: String,
        @Field("code") code: String,
    ): Result<Any>

    @POST("user/login/password")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Result<Login>

    @GET("user/profile")
    suspend fun getInfo(): Result<User>

    @PATCH("user/avatar")
    @Multipart
    suspend fun setAvatar(
        @Part imgFile: MultipartBody.Part
    ): Result<Any>


//
//    @POST("user/password")
//    @FormUrlEncoded
//    suspend fun resetPassword(
//        @Field("email") email: String
//    ): Result<Any>
//
//    @GET("user/{uid}")
//    suspend fun getUserData(
//        @Path("uid") uid: Long
//    ): Result<User>
//
//    @GET("user/avatar")
//    suspend fun getAvatar(
//        @Query("picture_name") pictureName: String
//    ): Result<Any>
//
//    @POST("user/login/password")
//    @FormUrlEncoded
//    suspend fun login(
//        @Field("username") username: String,
//        @Field("password") password: String
//    ): Result<User>
//

//
//    @GET("user/bind/github")
//    suspend fun bindGithub(
//        @Query("code") code: String
//    ): Result<Any>

}