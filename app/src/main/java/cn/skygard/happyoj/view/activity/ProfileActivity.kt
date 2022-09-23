package cn.skygard.happyoj.view.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeClipBounds
import android.transition.ChangeImageTransform
import android.transition.TransitionSet
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.Window
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import cn.skygard.common.base.ext.SP_DAY_NIGHT_MODE
import cn.skygard.common.base.ext.SP_DAY_NIGHT_PREFERENCE
import cn.skygard.common.base.ext.defaultSp
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.base.ui.BaseBindActivity
import cn.skygard.happyoj.R
import cn.skygard.happyoj.databinding.ActivityProfileBinding
import cn.skygard.happyoj.databinding.DialogSettingDayNightBinding
import cn.skygard.happyoj.domain.model.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class ProfileActivity : BaseBindActivity<ActivityProfileBinding>() {

    override val isCancelStatusBar: Boolean
        get() = true

    private val user by lazyUnlock {
        User.fromSp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initAnim()
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initAnim() {
        if (!intent.hasExtra(TransitionNameHeader)) return
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.sharedElementsUseOverlay = false
        val transitionSet = TransitionSet()
        transitionSet.addTransition(ChangeBounds())
        transitionSet.addTransition(ChangeClipBounds())
        transitionSet.addTransition(ChangeImageTransform())
        transitionSet.duration = 200
        window.sharedElementEnterTransition = transitionSet
        window.sharedElementExitTransition = transitionSet
        binding.ivHeader.transitionName = intent.getStringExtra(TransitionNameHeader)
    }

    private fun initView() {
        if (defaultSp.getBoolean(SP_DAY_NIGHT_PREFERENCE, true)) {
            binding.tvDayNightMode.text = "跟随系统"
        } else {
            binding.tvDayNightMode.text = "手动调节"
        }
        binding.settingDayNight.setOnClickListener {
            val dialogBinding = DialogSettingDayNightBinding.inflate(layoutInflater)
            val dialog = MaterialAlertDialogBuilder(this)
                .setTitle("黑夜模式")
                .setView(dialogBinding.root)
                .setNegativeButton("取消") { _, _ -> }.create()
            dialog.show()
            val onColor = ContextCompat.getColor(this, R.color.prim_on_color)
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(onColor)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(onColor)
            dialogBinding.rgDayNight.setOnCheckedChangeListener { group, checkedId ->
                val to = group.findViewById<RadioButton>(checkedId).text
                Log.d("ProfileActivity", "switch day night to $to")
                defaultSp.edit().run {
                    putBoolean(SP_DAY_NIGHT_PREFERENCE, checkedId == R.id.rb_follow_sys)
                    apply()
                }
                lifecycleScope.launch {
                    delay(200)
                    dialog.cancel()
                    binding.tvDayNightMode.text = if (defaultSp.getBoolean(SP_DAY_NIGHT_PREFERENCE, true)) {
                        "跟随系统"
                    } else {
                        "手动调节"
                    }
                }
            }
        }
    }

    companion object {

        const val TransitionNameHeader = "header"

        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, ProfileActivity::class.java))
        }

        fun startWithAnim(ctx: Context, headerTransient: Pair<View, String>) {
            assert(ctx is Activity)
            ctx.startActivity(Intent(ctx, ProfileActivity::class.java)
                .apply {
                       putExtra(TransitionNameHeader, headerTransient.second)
                },
                ActivityOptions.makeSceneTransitionAnimation(ctx as Activity, headerTransient).toBundle())
        }
    }
}