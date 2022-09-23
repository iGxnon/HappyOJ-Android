package cn.skygard.happyoj.repo.remote

import cn.skygard.happyoj.repo.remote.interceptor.AutoCookieInterceptor
import cn.skygard.happyoj.repo.remote.service.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private const val USER_BASE_URL = "http://43.143.10.229:11130/api/"
    private const val TASK_BASE_URL = "http://43.143.10.229:11140/api/"

    val userService: UserService by lazy { userRetrofit.create(UserService::class.java) }

    private val userRetrofit by lazy {
        initRetrofit(USER_BASE_URL)
    }

    private val taskRetrofit by lazy {
        initRetrofit(TASK_BASE_URL)
    }

    private fun initRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AutoCookieInterceptor())
            .build()
    }

}