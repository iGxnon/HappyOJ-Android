package cn.skygard.happyoj.domain.model

import cn.skygard.common.base.ext.defaultSp
import cn.skygard.happyoj.domain.logic.UserManager
import cn.skygard.happyoj.repo.database.entity.LoginUserEntity

data class User (
    val name: String,
    val email: String,
    val avatarUrl: String,
    val idToken: String,
    val accessToken: String,
    val refreshToken: String,
) {
    companion object {
        fun fromDatabase(user: LoginUserEntity): User {
            return User(
                name = user.name,
                avatarUrl = user.avatarUrl,
                email = user.email,
                idToken = user.idToken,
                accessToken = user.accessToken,
                refreshToken = user.refreshToken,
            )
        }

        fun fromSp(): User {
            assert(UserManager.checkLogin())
            return User(
                name = defaultSp.getString("name", "")!!,
                avatarUrl = defaultSp.getString("avatar_url", "")!!,
                email = defaultSp.getString("email", "")!!,
                idToken = defaultSp.getString("id_token", "")!!,
                accessToken = defaultSp.getString("access_token", "")!!,
                refreshToken = defaultSp.getString("refresh_token", "")!!,
            )
        }
    }
}
