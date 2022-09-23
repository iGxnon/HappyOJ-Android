package cn.skygard.happyoj.repo.remote.interceptor

import android.util.Log
import cn.skygard.happyoj.domain.logic.UserManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.*

class AutoCookieInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = if (UserManager.checkLogin()) {
            chain.request().newBuilder()
                .header("Cookie", UserManager.getAuthCookie())
                .build()
        }else {
            chain.request()
        }
        val resp = chain.proceed(request)
        resp.headers("set-cookie").forEach { cookie ->
            val cookies = cookie.split(";")
            val accessToken = cookies.find {
                it.startsWith("x-token=")
            }?.split("=")?.get(1)
            val refreshToken = cookies.find {
                it.startsWith("refresh-token=")
            }?.split("=")?.get(1)
            val idToken = cookies.find {
                it.startsWith("id-token=")
            }?.split("=")?.get(1)
            Log.d("AutoCookieInterceptor", cookie)
            accessToken?.run {
                Log.d("AutoCookieInterceptor", accessToken)
            }
            if (accessToken != null || refreshToken != null || idToken != null) {
                UserManager.refreshToken(accessToken, refreshToken, idToken)
            }
        }
        return resp
    }
}