package cn.skygard.happyoj.repo.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// 登录的账户列表
@Entity(tableName = "t_login_user")
data class LoginUserEntity(
    @PrimaryKey
    val uid: Int,  // 来自后端
    @ColumnInfo(name = "username")
    val name: String,
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String,
    @ColumnInfo(name = "id_token")
    val idToken: String,
    @ColumnInfo(name = "access_token")
    val accessToken: String,
    @ColumnInfo(name = "refresh_token")
    val refreshToken: String,
)