package cn.skygard.happyoj.repo.remote

import cn.skygard.happyoj.repo.remote.service.UserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private const val BASE_URL = "https://oj.gocybee.team/api/"

    val userService: UserService by lazy { retrofit.create(UserService::class.java) }

    private val retrofit by lazy {
        initRetrofit()
    }

    private fun initRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}