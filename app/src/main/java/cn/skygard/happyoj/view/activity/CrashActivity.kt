package cn.skygard.happyoj.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import cn.skygard.common.base.ext.asString
import cn.skygard.common.base.ui.BaseActivity
import cn.skygard.happyoj.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
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
            }.run {
                val dialog = create()
                // 这会导致崩溃页面崩溃（
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
//                    .setTextColor(ContextCompat.getColor(this@CrashActivity, R.color.prim_on_color))
                dialog
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