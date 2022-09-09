package cn.skygard.happyoj

import android.os.Handler
import android.os.Looper
import cn.skygard.common.base.BaseApp
import cn.skygard.happyoj.view.activity.CrashActivity

class HappyOJApp : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        // 全局抓取异常
        Handler(mainLooper).post {
            // 最多抓取三次异常
            repeat(3) {
                try {
                    Looper.loop()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        // 再多就崩溃
        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            e.printStackTrace()
            CrashActivity.start(appContext, e)
        }
    }

}