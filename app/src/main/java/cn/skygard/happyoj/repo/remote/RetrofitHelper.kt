package cn.skygard.happyoj.repo.remote

import cn.skygard.happyoj.repo.remote.interceptor.AutoCookieInterceptor
import cn.skygard.happyoj.repo.remote.service.RepoService
import cn.skygard.happyoj.repo.remote.service.TaskService
import cn.skygard.happyoj.repo.remote.service.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private const val BASE_URL = "https://oj.gocybee.team/api/"
    private const val GITHUB_BASE_URL = "https://api.github.com/"

    val userService: UserService by lazy { retrofit.create(UserService::class.java) }
    val taskService: TaskService by lazy { retrofit.create(TaskService::class.java) }
    val repoService: RepoService by lazy {
        Retrofit.Builder()
            .baseUrl(GITHUB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RepoService::class.java)
    }

    private val retrofit by lazy {
        initRetrofit()
    }

    private fun initRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
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