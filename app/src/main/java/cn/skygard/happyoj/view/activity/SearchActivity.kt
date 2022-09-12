package cn.skygard.happyoj.view.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import cn.skygard.happyoj.R
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.allViews
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cn.skygard.common.base.BaseApp
import cn.skygard.common.base.ext.invisible
import cn.skygard.common.base.ext.visible
import cn.skygard.common.base.ui.BaseBindActivity
import cn.skygard.common.mvi.BaseVmBindActivity
import cn.skygard.common.mvi.ext.observeEvent
import cn.skygard.common.mvi.ext.observeState
import cn.skygard.happyoj.databinding.ActivitySearchBinding
import cn.skygard.happyoj.intent.state.SearchAction
import cn.skygard.happyoj.intent.state.SearchEvent
import cn.skygard.happyoj.intent.state.SearchState
import cn.skygard.happyoj.intent.vm.SearchViewModel
import cn.skygard.happyoj.view.fragment.SearchFragment
import cn.skygard.happyoj.view.fragment.SearchHistoryFragment
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback

class SearchActivity : BaseVmBindActivity<SearchViewModel, ActivitySearchBinding>() {

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
        initAnim()
        initView()
        initViewState()
        initViewEvents()
    }

    private fun initViewState() {
        viewModel.viewStates.run {
            observeState(this@SearchActivity, SearchState::searchText) {
                binding.etInput.text.replace(0, binding.etInput.text.length, it)
            }
        }
    }

    private fun initViewEvents() {
        viewModel.viewEvents.observeEvent(this) {
            when (it) {
                is SearchEvent.ReplaceSearch -> {
                    replaceFragment(R.id.frag_container) {
                        SearchFragment.newInstance()
                    }
                }
                is SearchEvent.ReplaceHistory -> {
                    replaceFragment(R.id.frag_container) {
                        SearchHistoryFragment.newInstance()
                    }
                }
                is SearchEvent.StartSearch -> {
                    viewModel.dispatch(SearchAction.SearchFor(binding.etInput.text.toString()))
                }
            }
        }
    }

    private fun initAnim() {
        binding.searchBar.transitionName = intent.getStringExtra("transition_name")
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(binding.searchBar)
            duration = 300L
        }
        window.sharedElementExitTransition = MaterialContainerTransform().apply {
            addTarget(binding.searchBar)
            duration = 300L
        }
    }

    private fun initView() {
        replaceFragment(R.id.frag_container) {
            SearchHistoryFragment.newInstance()
        }
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
                if (it?.isNotEmpty() == true && binding.ivCleaner.visibility > View.VISIBLE) {
                    binding.ivCleaner.visible()
                    replaceFragment(R.id.frag_container) {
                        SearchFragment.newInstance()
                    }
                } else if (it?.isNotEmpty() == true) {
                    viewModel.dispatch(SearchAction.QuickSearchFor(it.toString()))
                } else {
                    binding.ivCleaner.invisible()
                    replaceFragment(R.id.frag_container) {
                        SearchHistoryFragment.newInstance()
                    }
                }
            }
            ivCleaner.setOnClickListener {
                etInput.text.clear()
            }

            etInput.setOnEditorActionListener { tv, actionId, _ ->
                val keyword = tv.text
                if (actionId == EditorInfo.IME_ACTION_SEARCH && keyword.isNotEmpty()) {
                    Log.d("SearchActivity", "search for $keyword")
                    viewModel.dispatch(SearchAction.SearchFor(keyword.toString()))
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