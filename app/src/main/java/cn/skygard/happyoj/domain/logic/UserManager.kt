package cn.skygard.happyoj.domain.logic

import android.util.Base64
import android.util.Log
import androidx.room.RoomDatabase
import cn.skygard.common.base.ext.defaultSp
import cn.skygard.happyoj.domain.model.User
import cn.skygard.happyoj.intent.state.LoginEvent
import cn.skygard.happyoj.repo.database.AppDatabase
import cn.skygard.happyoj.repo.database.entity.LoginUserEntity
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception

// TODO 优化那些丑陋的 try catch
object UserManager {

    suspend fun login(username: String, pwd: String): LoginEvent {
        try {
            val result = RetrofitHelper.userService.login(username, pwd)
            if (result.ok) {
                while (!checkLogin()) {
                    defaultSp.edit().putBoolean("is_login", true).apply()
                    val payload = result.data.oauth2Token.idToken.tokenValue.split(".")[1]
                    JSONObject(
                        Base64.decode(payload, Base64.DEFAULT).decodeToString()
                    ).getJSONObject("user_details").run {
                        AppDatabase.INSTANCE.loginUserDao().insert(
                            LoginUserEntity(
                                uid = getInt("id"),
                                name = getString("username"),
                                avatarUrl = getString("picture"),
                                email = getString("email"),
                                idToken = result.data.oauth2Token.idToken.tokenValue,
                                accessToken = result.data.oauth2Token.accessToken.tokenValue,
                                refreshToken = result.data.oauth2Token.refreshToken.tokenValue,
                            )
                        )
                    }
                }
                return LoginEvent.LoginSuccess
            }
        } catch (e: Exception) {
            if (e is HttpException) {
                Log.d("UserManager", e.response()?.errorBody()?.string()?:"")
            }
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
            if (e is HttpException) {
                Log.d("UserManager", e.response()?.errorBody()?.string()?:"")
            }
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
            if (e is HttpException) {
                Log.d("UserManager", e.response()?.errorBody()?.string()?:"")
            }
        }
        return LoginEvent.RegisterFailed
    }

    fun logout() = defaultSp.edit().putBoolean("is_login", false).apply()

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