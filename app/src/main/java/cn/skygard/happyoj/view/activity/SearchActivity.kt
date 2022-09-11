package cn.skygard.happyoj.view.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.ImageView
import cn.skygard.happyoj.R
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.allViews
import androidx.core.view.get
import androidx.core.view.size
import androidx.core.widget.addTextChangedListener
import cn.skygard.common.base.BaseApp
import cn.skygard.common.base.ext.invisible
import cn.skygard.common.base.ext.visible
import cn.skygard.common.base.ui.BaseBindActivity
import cn.skygard.happyoj.databinding.ActivitySearchBinding
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback

class SearchActivity : BaseBindActivity<ActivitySearchBinding>() {

    override val isCancelStatusBar: Boolean
        get() = false

    override val statusBarColor: Int
        get() = ContextCompat.getColor(this, R.color.prime_color_variant)

    override fun onCreate(savedInstanceState: Bundle?) {
        if (BaseApp.darkMode) {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        } else {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        }
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        super.onCreate(savedInstanceState)
        binding.searchBar.transitionName = intent.getStringExtra("transition_name")
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(binding.searchBar)
            duration = 300L
        }
        window.sharedElementExitTransition = MaterialContainerTransform().apply {
            addTarget(binding.searchBar)
            duration = 300L
        }
        initView()
    }

    private fun initView() {
        binding.run {
            setSupportActionBar(searchBar)
            supportActionBar?.run {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_anim_menu_2_back)
                title = ""
            }
            // 启动所有可以启动的动画
            // 找不到 toolbar 上面的那个返回键（
            searchBar.allViews.forEach {
                if (it is ImageView) {
                    if (it.drawable is Animatable) {
                        (it.drawable as Animatable).start()
                    }
                }
            }

            // 清除键动态显示
            etInput.addTextChangedListener {
                if (it?.isNotEmpty() == true) {
                    binding.ivCleaner.visible()
                } else {
                    binding.ivCleaner.invisible()
                }
            }
            ivCleaner.setOnClickListener {
                etInput.text.clear()
            }

            etInput.setOnEditorActionListener { tv, actionId, _ ->
                val keyword = tv.text
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d("SearchActivity", "search for $keyword")
                }
                false
            }

            // 自动弹出输入法
            etInput.postDelayed({
                binding.etInput.requestFocus()
                BaseApp.appContext.getSystemService(INPUT_METHOD_SERVICE).run {
                    (this as InputMethodManager).showSoftInput(binding.etInput,
                        InputMethodManager.SHOW_IMPLICIT)
                }
            }, 350)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(ctx: Context, viewGroup: ViewGroup, transitionName: String) {
            val intent = Intent(ctx, SearchActivity::class.java).apply {
                putExtra("transition_name", transitionName)
            }
            if (ctx is Activity) {
                ctx.startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(ctx, viewGroup, transitionName).toBundle())
            } else {
                ctx.startActivity(intent)
            }
        }
    }

}