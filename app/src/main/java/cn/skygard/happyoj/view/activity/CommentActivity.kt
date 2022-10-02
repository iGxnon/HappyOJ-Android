package cn.skygard.happyoj.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.skygard.common.base.BaseApp
import cn.skygard.common.base.ext.gone
import cn.skygard.common.base.ext.visible
import cn.skygard.common.base.ui.BaseBindActivity
import cn.skygard.common.mvi.BaseVmBindActivity
import cn.skygard.common.mvi.ext.observeState
import cn.skygard.happyoj.R
import cn.skygard.happyoj.databinding.ActivityCommentBinding
import cn.skygard.happyoj.intent.state.CommentAction
import cn.skygard.happyoj.intent.state.CommentState
import cn.skygard.happyoj.intent.state.FetchState
import cn.skygard.happyoj.intent.vm.CommentViewModel
import cn.skygard.happyoj.intent.vm.LabViewModel
import cn.skygard.happyoj.view.adapter.CheckpointsRvAdapter
import cn.skygard.happyoj.view.adapter.CommentRvAdapter
import cn.skygard.happyoj.view.adapter.SubmitCheckpointsRvAdapter

class CommentActivity : BaseVmBindActivity<CommentViewModel, ActivityCommentBinding>() {

    private val tid by lazy {
        intent.getLongExtra("task_id", -1L)
    }

    private val title by lazy {
        intent.getStringExtra("title")
    }

    override val viewModelFactory: ViewModelProvider.Factory
        get() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                if (modelClass.isAssignableFrom(CommentViewModel::class.java)) {
                    return CommentViewModel(tid) as T
                }
                throw IllegalArgumentException("unknown view model class")
            }
        }

    override val navigationBarColor: Int
        get() = ContextCompat.getColor(this, R.color.prime_color)

    override val statusBarColor: Int
        get() = ContextCompat.getColor(this, R.color.prime_color)

    override val isCancelStatusBar: Boolean
        get() = false

    private val commentRvAdapter by lazy {
        CommentRvAdapter()
    }

    private val checkpointsRvAdapter by lazy {
        SubmitCheckpointsRvAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (BaseApp.darkMode) {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        } else {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        }
        super.onCreate(savedInstanceState)
        initView()
        initViewStates()
    }

    private fun initView() {
        binding.run {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
            }
            supportActionBar?.title = title
            rvComment.run {
                adapter = commentRvAdapter
                layoutManager = LinearLayoutManager(this@CommentActivity)
            }
            rvSubmitCheckpoints.run {
                adapter = checkpointsRvAdapter
                layoutManager = LinearLayoutManager(this@CommentActivity)
            }
        }
        viewModel.dispatch(CommentAction.GetData)
    }

    private fun initViewStates() {
        viewModel.viewStates.run {
            observeState(this@CommentActivity, CommentState::comments) {
                if (it.isEmpty()) {
                    binding.tvNoComment.visible()
                } else {
                    binding.tvNoComment.gone()
                }
                commentRvAdapter.submitList(it)
            }
            observeState(this@CommentActivity, CommentState::submits) {
                checkpointsRvAdapter.submitList(it)
            }
            observeState(this@CommentActivity, CommentState::fetchState) {
                when (it) {
                    FetchState.NotFetched -> "获取失败".toast()
                    else -> {}
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(ctx: Context, tid: Long, title: String) {
            ctx.startActivity(Intent(ctx, CommentActivity::class.java).apply {
                putExtra("task_id", tid)
                putExtra("title", title)
            })
        }
    }
}