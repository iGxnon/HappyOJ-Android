package cn.skygard.happyoj.repo.database.dao

import androidx.room.*
import cn.skygard.happyoj.repo.database.entity.LoginUserEntity

@Dao
interface LoginUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userEntities: MutableList<LoginUserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userEntity: LoginUserEntity)

    @Delete
    suspend fun delete(userEntity: LoginUserEntity)

    @Query("SELECT id_token FROM t_login_users WHERE uid = :uid")
    suspend fun getIdToken(uid: Int): String?

    @Query("SELECT access_token FROM t_login_users WHERE uid = :uid")
    suspend fun getAccessToken(uid: Int): String?

    @Query("SELECT refresh_token FROM t_login_users WHERE uid = :uid")
    suspend fun getRefreshToken(uid: Int): String?

}