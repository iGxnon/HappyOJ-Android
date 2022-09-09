package cn.skygard.happyoj.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.skygard.common.base.ext.asString
import cn.skygard.common.base.ui.BaseActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.system.exitProcess

class CrashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val exception = intent.getSerializableExtra("exception") as Throwable
        MaterialAlertDialogBuilder(this)
            .setTitle("APP 崩溃！")
            .setMessage("""
                ${exception.asString()}
                ${exception.stackTrace.joinToString("\n")}
            """.trimIndent())
            .setPositiveButton("退出应用") { _, _ ->
                finish()
                exitProcess(0)
            }.show()
    }

    companion object {
        fun start(ctx: Context, e: Throwable) {
            ctx.startActivity(Intent(ctx, CrashActivity::class.java)
                                .putExtra("exception", e)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            exitProcess(0)
        }
    }
}