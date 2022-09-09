package cn.skygard.happyoj.view.activity

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import cn.skygard.common.base.ext.gone
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.base.ext.visible
import cn.skygard.common.mvi.BaseVmBindActivity
import cn.skygard.common.mvi.ext.observeEvent
import cn.skygard.happyoj.R
import cn.skygard.happyoj.databinding.ActivityMainBinding
import cn.skygard.happyoj.intent.state.MainSharedEvent
import cn.skygard.happyoj.intent.vm.MainViewModel
import cn.skygard.happyoj.view.fragment.SubmitsFragment
import cn.skygard.happyoj.view.fragment.TasksFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay

class MainActivity : BaseVmBindActivity<MainViewModel, ActivityMainBinding>() {

    override val isCancelStatusBar: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firstStart = intent.getBooleanExtra("firstStart", true)
        initView(firstStart)
        initViewStates()
        initViewEvents()
        if (!firstStart) {
            switchThemeAnim()
        }
    }

    private var onGlobalLayout : ViewTreeObserver.OnGlobalLayoutListener? = null
    private var mAnimReveal : Animator? = null

    private val navHeader by lazyUnlock {
        binding.navView.inflateHeaderView(R.layout.nav_header)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun switchThemeAnim() {
        val v = window.decorView
        v.visible()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }

        onGlobalLayout = ViewTreeObserver.OnGlobalLayoutListener { //此时既是开始揭露动画的最佳时机
            mAnimReveal?.removeAllListeners()
            mAnimReveal?.cancel()
            val pivotView = navHeader.findViewById<AppCompatImageView>(R.id.iv_switch_dayNight)
            mAnimReveal = ViewAnimationUtils.createCircularReveal(v,
                pivotView.x.toInt(),
                pivotView.y.toInt(),
                0f,
                v.height.toFloat()
            )
            mAnimReveal?.duration = 400
            mAnimReveal?.addListener(onEnd = {
                onGlobalLayout?.let {
                    // 我们需要在揭露动画进行完后及时移除回调
                    v.viewTreeObserver.removeOnGlobalLayoutListener(it)
                }
            })
            mAnimReveal?.start()
        }
        v.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayout)
    }

    private fun showStatusBar() {
        val window = this.window
        val decorView = window.decorView
        WindowCompat.setDecorFitsSystemWindows(window, true)
        val windowInsetsController = WindowCompat.getInsetsController(window, decorView)
        if (delegate.localNightMode == AppCompatDelegate.MODE_NIGHT_NO) {
            Log.d("MainActivity", "Set status bar into light mode(set text into black)")
        } else {
            Log.d("MainActivity", "Set status bar into dark mode(set text into white)")
        }
        windowInsetsController?.isAppearanceLightStatusBars = delegate.localNightMode == AppCompatDelegate.MODE_NIGHT_NO
        window.statusBarColor = ContextCompat.getColor(this, R.color.prime_color_dark)
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

    private fun initView(firstStart: Boolean = true) {
        if (firstStart) {
            binding.splashScreen.visible()
            binding.drawer.gone()
            binding.splashScreen.postDelayed({
                binding.splashScreen.gone()
                binding.drawer.visible()
                Log.d("MainActivity", "entered MainActivity")
                showStatusBar()
            }, 2000)
        } else {
            binding.splashScreen.gone()
            binding.drawer.visible()
            Log.d("MainActivity", "entered MainActivity")
            showStatusBar()
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
            findViewById<AppCompatImageView>(R.id.iv_switch_dayNight).setOnClickListener {
                if (delegate.localNightMode == AppCompatDelegate.MODE_NIGHT_NO) { // 白天
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
                }
                intent.putExtra("firstStart", false)
                recreate()
                Log.d("MainActivity", "switch theme")
            }
        }

        binding.ivDrawerOpen.setOnClickListener {
            binding.drawer.openDrawer(binding.navView)
        }

        binding.navView.apply {
            setCheckedItem(R.id.nav_tasks)
            setNavigationItemSelectedListener {
                // start new activity/fragment here
                when (it.itemId) {
                    R.id.nav_account -> {
                        Log.d("MainActivity", "navigation to account")
                        switchToolbar(true)
                    }
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

    private fun initViewStates() {

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