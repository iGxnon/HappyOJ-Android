package cn.skygard.common.base

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import kotlin.properties.Delegates

/**
 * kim.bifrost.lib_common.BaseApp
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/13 23:10
 */
open class BaseApp : Application() {
    @CallSuper
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        // 全局主题
        var darkMode by Delegates.notNull<Boolean>()

        lateinit var appContext: Context
            private set
    }
}