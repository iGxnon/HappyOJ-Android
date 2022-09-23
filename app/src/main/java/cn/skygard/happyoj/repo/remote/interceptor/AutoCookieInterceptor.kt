package cn.skygard.happyoj.repo.remote.interceptor

import cn.skygard.happyoj.domain.logic.UserManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AutoCookieInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = if (UserManager.checkLogin()) {
            chain.request().newBuilder()
                .header("cookie", UserManager.getAuthCookie())
                .build()
        }else {
            chain.request()
        }
        val resp = chain.proceed(request)
        resp.header("set-cookie")?.run {
            val cookies = this.split(";")
            val accessToken = cookies.find {
                it.startsWith("x-token=")
            }?.split("=")?.get(1)
            val refreshToken = cookies.find {
                it.startsWith("refresh-token=")
            }?.split("=")?.get(1)
            val idToken = cookies.find {
                it.startsWith("id-token=")
            }?.split("=")?.get(1)
            if (accessToken != null || refreshToken != null || idToken != null) {
                UserManager.refreshToken(accessToken, refreshToken, idToken)
            }
        }
        return resp
    }
}