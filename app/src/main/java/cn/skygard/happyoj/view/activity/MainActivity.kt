package cn.skygard.happyoj.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import cn.skygard.common.base.BaseApp
import cn.skygard.common.base.ext.defaultSp
import cn.skygard.common.base.ext.gone
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.base.ext.visible
import cn.skygard.common.mvi.BaseVmBindActivity
import cn.skygard.common.mvi.ext.observeEvent
import cn.skygard.happyoj.R
import cn.skygard.happyoj.databinding.ActivityMainBinding
import cn.skygard.happyoj.intent.state.MainSharedEvent
import cn.skygard.happyoj.intent.vm.MainViewModel
import cn.skygard.happyoj.domain.logic.UserManager
import cn.skygard.happyoj.domain.model.User
import cn.skygard.happyoj.intent.state.MainAction
import cn.skygard.happyoj.view.fragment.SubmitsFragment
import cn.skygard.happyoj.view.fragment.TasksFragment
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback

class MainActivity : BaseVmBindActivity<MainViewModel, ActivityMainBinding>() {

    override val isCancelStatusBar: Boolean
        get() = false

    override val statusBarColor: Int
        get() = ContextCompat.getColor(this, R.color.prime_color)

    private val navHeader by lazyUnlock {
        binding.navView.inflateHeaderView(R.layout.nav_header)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 启用转场动画
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false
        super.onCreate(savedInstanceState)
        val firstStart = intent.getBooleanExtra("firstStart", true)
        initView(firstStart)
        initViewEvents()
    }

    override fun onResume() {
        super.onResume()
        initView(false)
    }

    /**
     * 切换 Toolbar
     * 一开始的 Toolbar 是 funcToolbar
     */
    private fun switchToolbar(baseToolbar: Boolean) {
        if (baseToolbar) {
            binding.funcToolbar.gone()
            binding.toolbar.visible()
        } else {
            binding.toolbar.gone()
            binding.funcToolbar.visible()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun initView(firstStart: Boolean = true) {
        if (firstStart) {
            binding.splashScreen.visible()
            binding.drawer.gone()
            binding.splashScreen.postDelayed({
                binding.splashScreen.gone()
                binding.drawer.visible()
                Log.d("MainActivity", "first entered MainActivity")
            }, 2000)
        } else {
            binding.splashScreen.gone()
            binding.drawer.visible()
            Log.d("MainActivity", "entered MainActivity")
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_24)
        }

        if (firstStart) {
            replaceFragment(R.id.frag_container) {
                TasksFragment.newInstance()
            }
        }

        navHeader.run {
            findViewById<AppCompatImageView>(R.id.iv_switch_dayNight).setOnClickListener { view ->
                if (BaseApp.darkMode) { // 黑夜
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
                    BaseApp.darkMode = false
                } else {
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
                    BaseApp.darkMode = true
                }
                super.switchDayNight()
                intent.putExtra("firstStart", false)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    window.decorView.invalidate()
                    return@setOnClickListener
                }
                val pivot = IntArray(2)
                view.getLocationInWindow(pivot)
                // 通过展开动画来触发 View 树重新绘制，不过有时候会卡一下，很不舒服（
                // 可以尝试优化 MainActivity 的 UI 结构加快加载，应该就不会太卡了
                // TODO 优化
                val anim = ViewAnimationUtils.createCircularReveal(
                    window.decorView,
                    pivot[0] + view.width / 2,
                    pivot[1] + view.height / 2,
                    0f,
                    window.decorView.height.toFloat()
                )
                anim.interpolator = AccelerateDecelerateInterpolator()
                anim.startDelay = 15
                anim.duration = 600
                anim.start()
            }
            val sivProfile = findViewById<ShapeableImageView>(R.id.sivProfileOpen)
            val profileName = findViewById<TextView>(R.id.profileName)
            val email = findViewById<TextView>(R.id.email)
            if (UserManager.checkLogin()) {
                viewModel.dispatch(MainAction.RefreshData)
                sivProfile.setOnClickListener {
                    it.transitionName = "profile-opener-header"
                    ProfileActivity.startWithAnim(this@MainActivity,
                        Pair.create(it, "profile-opener-header"))
                }
                val user = User.fromSp()
                profileName.text = user.name
                email.text = user.email
                Glide.with(this@MainActivity)
                    .load(user.avatarUrl)
                    .into(sivProfile)
            } else {
                profileName.text = "未登录"
                email.text = "未登录"
                sivProfile.setOnClickListener {
                    LoginActivity.start(this@MainActivity)
                }
                Glide.with(this@MainActivity)
                    .load(R.drawable.default_avatar)
                    .into(sivProfile)
            }
        }

        binding.run {
            searchOpen.setOnClickListener {
                Log.d("MainActivity", "open search")
                binding.searchBar.transitionName = "search"
                SearchActivity.start(this@MainActivity, binding.searchBar, "search")
            }
            if (UserManager.checkLogin()) {
                viewModel.dispatch(MainAction.RefreshData)
                ivProfileOpen.setOnClickListener {
                    Log.d("MainActivity", "open profile")
                    binding.ivProfileOpen.transitionName = "profile-opener-header"
                    ProfileActivity.startWithAnim(this@MainActivity,
                        Pair.create(binding.ivProfileOpen, "profile-opener-header"))
                }
                val user = User.fromSp()
                Glide.with(this@MainActivity)
                    .load(user.avatarUrl)
                    .into(ivProfileOpen)
            } else {
                ivProfileOpen.setOnClickListener {
                    LoginActivity.start(this@MainActivity)
                }
                Glide.with(this@MainActivity)
                    .load(R.drawable.default_avatar)
                    .into(ivProfileOpen)
            }

            ivDrawerOpen.setOnClickListener {
                drawer.openDrawer(binding.navView)
            }

            navView.apply {
                setCheckedItem(R.id.nav_tasks)
                setNavigationItemSelectedListener {
                    when (it.itemId) {
                        R.id.nav_tasks -> {
                            Log.d("MainActivity", "navigation to tasks")
                            switchToolbar(false)
                            replaceFragment(R.id.frag_container) {
                                TasksFragment.newInstance()
                            }
                        }
                        R.id.nav_submits -> {
                            Log.d("MainActivity", "navigation to submits")
                            switchToolbar(true)
                            replaceFragment(R.id.frag_container) {
                                SubmitsFragment.newInstance()
                            }
                        }
                    }
                    binding.drawer.closeDrawer(binding.navView)
                    true
                }
            }
        }
    }

    private fun initViewEvents() {
        viewModel.viewEvents.observeEvent(this) {
            when (it) {
                is MainSharedEvent.ShowSnack -> {
                    Snackbar.make(binding.root, it.mess, Snackbar.LENGTH_SHORT)
                        .setAction(it.action, it.actionCallback)
                        .show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                binding.drawer.openDrawer(binding.navView)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(ctx: Context, enableSplash: Boolean = true) {
            ctx.startActivity(Intent(ctx, MainActivity::class.java).apply {
                putExtra("firstStart", enableSplash)
            })
        }
    }
}