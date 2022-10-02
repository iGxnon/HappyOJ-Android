package cn.skygard.happyoj.domain.logic

import android.util.Log
import cn.skygard.common.base.ext.defaultSp
import cn.skygard.happyoj.domain.model.User
import cn.skygard.happyoj.intent.state.LoginEvent
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import cn.skygard.happyoj.repo.remote.model.Result
import java.lang.Exception

// TODO 优化那些丑陋的 try catch
object UserManager {

    suspend fun login(username: String, pwd: String): LoginEvent {
        try {
            val result = RetrofitHelper.userService.login(username, pwd)
            if (result.ok) {
                while (!checkLogin()) {
                    val editor = defaultSp.edit()
                    editor.putBoolean("is_login", true)
                    editor.apply()
                }
                return LoginEvent.LoginSuccess
            }
        } catch (e: Exception) {
            Result.onError(e)
        }
        return LoginEvent.LoginFailed
    }

    suspend fun registerMail(email: String): LoginEvent {
        try {
            RetrofitHelper.userService.registerMail(email).run {
                if (this.ok) {
                    return LoginEvent.MailSuccess
                }
            }
        } catch (e: Exception) {
            Result.onError(e)
        }
        return LoginEvent.MailFailed
    }

    suspend fun register(username: String, email: String, stuId: String,
                         code: String, pwd: String, name: String): LoginEvent {
        try {
            RetrofitHelper.userService.register(username, pwd, email, stuId, name, code).run {
                if (this.ok) {
                    return LoginEvent.RegisterSuccess
                }
            }
        } catch (e: Exception) {
            Result.onError(e)
        }
        return LoginEvent.RegisterFailed
    }

    fun logout() {

        defaultSp.edit()
            .putBoolean("is_login", false)
            .putString("email", "")
            .putString("name", "")
            .putString("username", "")
            .putInt("stu_id", -1)
            .putString("id_token", "")
            .putString("refresh_token", "")
            .putString("access_token", "")
            .putString("avatar_url", "")
            .apply()
    }

    fun checkLogin() = defaultSp.getBoolean("is_login", false)

    fun refreshToken(accessToken: String?, refreshToken: String?, idToken: String?) {
        val editor = defaultSp.edit()
        editor.run {
            accessToken?.run {
                putString("access_token", accessToken)
            }
            refreshToken?.run {
                putString("refresh_token", refreshToken)
            }
            idToken?.run {
                putString("id_token", idToken)
            }
            apply()
        }
    }

    fun getAuthCookie(): String {
        val user = User.fromSp()
        Log.d("UserManager", "get an auth token")
        return "x-token=${user.accessToken}; refresh-token=${user.refreshToken}; id-token=${user.idToken}"
    }
}