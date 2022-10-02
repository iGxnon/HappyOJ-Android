package cn.skygard.happyoj.view.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.transition.ChangeBounds
import android.transition.ChangeClipBounds
import android.transition.ChangeImageTransform
import android.transition.TransitionSet
import android.util.Log
import android.util.Pair
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.lifecycle.lifecycleScope
import cn.skygard.common.base.BaseApp
import cn.skygard.common.base.ext.SP_DAY_NIGHT_MODE
import cn.skygard.common.base.ext.SP_DAY_NIGHT_PREFERENCE
import cn.skygard.common.base.ext.defaultSp
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.base.ui.BaseBindActivity
import cn.skygard.happyoj.R
import cn.skygard.happyoj.databinding.ActivityProfileBinding
import cn.skygard.happyoj.databinding.DialogSettingDayNightBinding
import cn.skygard.happyoj.domain.logic.UserManager
import cn.skygard.happyoj.domain.model.User
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import cn.skygard.happyoj.repo.remote.model.Result
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.wanglu.photoviewerlibrary.PhotoViewer
import com.wanglu.photoviewerlibrary.photoview.PhotoView
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Error
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

class ProfileActivity : BaseBindActivity<ActivityProfileBinding>() {

    override val isCancelStatusBar: Boolean
        get() = true

    private val mAvatar = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        try {
            contentResolver.openInputStream(it)?.readBytes()?.let { bytes ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val result = RetrofitHelper.userService.setAvatar(
                            MultipartBody.Part.createFormData(
                                "file", "avatar.jpeg", RequestBody.create(
                                    MediaType.get("image/jpeg"),
                                    bytes
                                ))
                        )
                        Log.d("ProfileActivity", result.message)
                        withContext(Dispatchers.Main) {
                            "上传成功".toast()
                        }
                    } catch (e: Exception) {
                        Result.onError(e)
                    }
                }
            }
            Glide.with(this@ProfileActivity)
                .load(it)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.err_avatar)
                .into(binding.ivHeader)
        } catch (ignore: Throwable) { }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (BaseApp.darkMode) {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        } else {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        }
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        super.onCreate(savedInstanceState)
        window.enterTransition = MaterialFadeThrough().apply {
            duration = 200L
        }
        window.exitTransition = MaterialFadeThrough().apply {
            duration = 200L
        }
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    private fun initView() {
        val user = User.fromSp()
        binding.run {
            if (defaultSp.getBoolean(SP_DAY_NIGHT_PREFERENCE, true)) {
                tvDayNightMode.text = "跟随系统"
            } else {
                tvDayNightMode.text = "手动调节"
            }
            setSupportActionBar(toolbar)
            supportActionBar?.run {
                setDisplayHomeAsUpEnabled(true)
            }
            toolbar.setNavigationOnClickListener {
                finishAfterTransition()
            }
            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.profile_quit -> {
                        Log.d("ProfileActivity", "logout")
                        UserManager.logout()
                        finishAfterTransition()
                    }
                    R.id.profile_avatar -> {
                        mAvatar.launch(arrayOf("image/jpeg"))
                    }
                }
                true
            }
            settingDayNight.setOnClickListener {
                showOptionDayNight()
            }
            headerLayout.title = user.username
            Glide.with(this@ProfileActivity)
                .load(user.avatarUrl)
                .signature(ObjectKey(""))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.err_avatar)
                .into(ivHeader)
            tvUsername.text = user.username
            tvName.text = user.name
            tvStuId.text = user.stuId.toString()
            tvEmail.text = user.email
            ivHeader.setOnClickListener {
                PhotoViewer
                    .setClickSingleImg(user.avatarUrl, ivHeader)
                    .setShowImageViewInterface(object : PhotoViewer.ShowImageViewInterface {
                        override fun show(iv: ImageView, url: String) {
                            Glide.with(iv.context).load(user.avatarUrl).into(iv)
                        }
                    }).start(this@ProfileActivity)
            }
        }
    }

    private fun showOptionDayNight() {
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

    companion object {
        fun start(ctx: Context) {
            if (ctx is Activity) {
                ctx.startActivity(Intent(ctx, ProfileActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(ctx).toBundle())
            } else {
                ctx.startActivity(Intent(ctx, ProfileActivity::class.java))
            }
        }
    }
}