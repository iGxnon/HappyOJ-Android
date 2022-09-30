package cn.skygard.happyoj.domain.model

import cn.skygard.common.base.ext.defaultSp
import cn.skygard.happyoj.domain.logic.UserManager
import cn.skygard.happyoj.repo.database.entity.LoginUserEntity

data class User (
    val name: String,
    val username: String,
    val email: String,
    val avatarUrl: String,
    val idToken: String,
    val stuId: Int,
    val accessToken: String,
    val refreshToken: String,
) {
    companion object {

        fun fromSp(): User {
            assert(UserManager.checkLogin())
            return User(
                name = defaultSp.getString("name", "")!!,
                username = defaultSp.getString("username", "")!!,
                avatarUrl = defaultSp.getString("avatar_url", "")!!,
                email = defaultSp.getString("email", "")!!,
                idToken = defaultSp.getString("id_token", "")!!,
                stuId = defaultSp.getInt("stu_id", -1),
                accessToken = defaultSp.getString("access_token", "")!!,
                refreshToken = defaultSp.getString("refresh_token", "")!!,
            )
        }
    }
}
