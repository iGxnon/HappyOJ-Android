package cn.skygard.happyoj.repo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cn.skygard.common.base.BaseApp
import cn.skygard.happyoj.repo.database.converter.DateConverter
import cn.skygard.happyoj.repo.database.dao.LoginUserDao
import cn.skygard.happyoj.repo.database.entity.LoginUserEntity

@Database(entities = [LoginUserEntity::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun loginUserDao(): LoginUserDao

    companion object {

        @JvmStatic
        val INSTANCE by lazy { create(BaseApp.appContext) }

        private fun create(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "happy_oj.db")
                .allowMainThreadQueries()
                .build()
    }

}