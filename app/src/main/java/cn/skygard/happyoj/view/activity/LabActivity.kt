package cn.skygard.happyoj.view.activity

import android.animation.ValueAnimator
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.skygard.common.base.BaseApp
import cn.skygard.common.base.adapter.BaseVPAdapter
import cn.skygard.common.base.ext.color
import cn.skygard.common.base.ext.dp2px
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.mvi.BaseVmBindActivity
import cn.skygard.common.mvi.ext.observeEvent
import cn.skygard.common.mvi.ext.observeState
import cn.skygard.happyoj.R
import cn.skygard.happyoj.databinding.ActivityLabBinding
import cn.skygard.happyoj.databinding.DialogFeedbackBinding
import cn.skygard.happyoj.databinding.DialogRepoSubmitBinding
import cn.skygard.happyoj.intent.state.LabAction
import cn.skygard.happyoj.intent.state.LabEvent
import cn.skygard.happyoj.intent.state.LabState
import cn.skygard.happyoj.intent.state.Pages
import cn.skygard.happyoj.intent.vm.LabViewModel
import cn.skygard.happyoj.repo.database.AppDatabase
import cn.skygard.happyoj.repo.remote.model.Task
import cn.skygard.happyoj.view.fragment.LabCommitFragment
import cn.skygard.happyoj.view.fragment.LabDetailFragment
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.regex.Pattern
import com.google.android.material.transition.platform.MaterialFadeThrough

class LabActivity : BaseVmBindActivity<LabViewModel, ActivityLabBinding>() {

    override val isCancelStatusBar: Boolean
        get() = true

    private val taskItem by lazyUnlock {
        Task.TaskSubject(
            id = intent.getLongExtra("task_id", -1L),
            title = intent.getStringExtra("task_title")!!,
            imageUrl = intent.getStringExtra("task_img")!!,
            updateTime = intent.getStringExtra("task_date")!!,
            summary = intent.getStringExtra("task_summary")!!,
        )
    }

    override val viewModelFactory: ViewModelProvider.Factory
        get() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                if (modelClass.isAssignableFrom(LabViewModel::class.java)) {
                    return LabViewModel(taskItem) as T
                }
                throw IllegalArgumentException("unknown view model class")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (BaseApp.darkMode) {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        } else {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        }
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        super.onCreate(savedInstanceState)
        window.enterTransition = MaterialFadeThrough().apply {
            duration = 200L
        }
        window.exitTransition = MaterialFadeThrough().apply {
            duration = 200L
        }
        initView()
        initViewState()
        initViewEvent()
    }

    private fun initViewEvent() {
        viewModel.viewEvents.observeEvent(this) {
            when (it) {
                LabEvent.SubmitFailed -> {
                    "提交失败".toast()
                }
                LabEvent.SubmitSuccess -> {
                    "提交成功".toast()
                }
                else -> {}
            }
        }
    }

    private fun initViewState() {
        viewModel.viewStates.run {
            observeState(this@LabActivity, LabState::currentPage) {
                binding.vp2.currentItem = it.index
            }
            observeState(this@LabActivity, LabState::menuVisibility) {
                val fromDp: Int
                val toDp: Int
                val show: Boolean
                if (it) {
                    Log.d("LabActivity", "open menu")
                    fromDp = 30.dp2px()
                    toDp = 0
                    show = true
                } else {
                    Log.d("LabActivity", "close menu")
                    fromDp = 0
                    toDp = 30.dp2px()
                    show = false
                }
                binding.run {
                    if (show)
                        fabMenu.showMenu(true)
                    else
                        fabMenu.hideMenu(true)
                    ValueAnimator.ofInt(fromDp, toDp).run {
                        interpolator = AccelerateDecelerateInterpolator()
                        addUpdateListener { v ->
                            tabLayout.setPadding(
                                tabLayout.paddingLeft,
                                v.animatedValue as Int,
                                tabLayout.paddingRight,
                                tabLayout.paddingBottom
                            )
                        }
                        start()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.lab_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun randomTint() : Int {
        return listOf(
            R.color.green_bg_tint,
            R.color.pink_bg_tint,
            R.color.purple_bg_tint,
            R.color.yellow_bg_tint
        )[random.nextInt(4)]
    }

    private fun initView() {
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.run {
                setDisplayHomeAsUpEnabled(true)
                title = taskItem.title
            }

            vp2.adapter = BaseVPAdapter(
                supportFragmentManager,
                lifecycle,
                Pages.all(),
            ) { _, i ->
                return@BaseVPAdapter when(i) {
                    Pages.LAB.index -> LabDetailFragment.newInstance()
                    Pages.COMMIT.index -> LabCommitFragment.newInstance()
                    else -> Fragment()
                }
            }

            TabLayoutMediator(tabLayout, vp2, true, true) { tab, pos ->
                tab.text = when (pos) {
                    Pages.LAB.index -> Pages.LAB.title
                    Pages.COMMIT.index -> Pages.COMMIT.title
                    else -> ""
                }
                Log.d("LabActivity", "$pos")
            }.attach()

            tvDesc.text = taskItem.summary
            if (taskItem.imageUrl != "") {
                Glide.with(this@LabActivity)
                    .load(taskItem.imageUrl)
                    .into(ivBackground)
            }
            randomTint().run {
                ivBgTint.setImageResource(this)
                tabLayout.setSelectedTabIndicatorColor(this.color)
            }

            randomTint().color.run {
                fabFavor.colorNormal = this
                fabFavor.colorPressed = this
            }
            randomTint().color.run {
                fabTop.colorNormal = this
                fabTop.colorPressed = this
            }
            randomTint().color.run {
                fabRepo.colorNormal = this
                fabRepo.colorPressed = this
            }
            randomTint().color.run {
                fabMenu.menuButtonColorNormal = this
                fabMenu.menuButtonColorPressed = this
            }

            fabTop.setOnClickListener {
                Log.d("LabActivity", "scroll to top.")
                viewModel.dispatch(LabAction.ScrollToTop)
            }
            fabRepo.setOnClickListener {
                showSubmitRepoUrl()
            }
        }
    }

    private fun showFeedback() {
        val dialogBinding = DialogFeedbackBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("反馈")
            .setView(dialogBinding.root)
            .setPositiveButton("提交") { _, _ ->
                val email = dialogBinding.etEmail.text
                val feedback = dialogBinding.etFeedback.text
                if (!Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$")
                        .matcher(email).matches()) {
                    toast("邮箱格式不正确！")
                    return@setPositiveButton
                }
                if (feedback.length > 150) {
                    toast("反馈过长！")
                    return@setPositiveButton
                }
                Log.d("LabActivity", "Email: $email")
                Log.d("LabActivity", "Feedback: $feedback")
                // TODO 提交
                Snackbar.make(binding.root, "提交成功", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消"){_, _ -> }.create()
        dialog.show()
        val onColor = ContextCompat.getColor(this, R.color.prim_on_color)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(onColor)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(onColor)
    }

    private fun showSubmitRepoUrl() {
        val dialogBinding = DialogRepoSubmitBinding.inflate(layoutInflater)
        CoroutineScope(Dispatchers.IO).launch {
            val hasSubmit = AppDatabase.INSTANCE.taskDao().getRepoUrl(taskItem.id) != ""
            withContext(Dispatchers.Main) {
                val title = if (hasSubmit) {
                    "修改仓库"
                } else {
                    "提交仓库"
                }
                val dialog = MaterialAlertDialogBuilder(this@LabActivity)
                    .setTitle(title)
                    .setView(dialogBinding.root)
                    .setPositiveButton(if (hasSubmit) "提交" else "修改") { _, _ ->
                        val repoUrl = dialogBinding.etRepoUrl.text.toString()
                        val desc = dialogBinding.etDesc.text.toString()
                        Log.d("LabActivity", "RepoUrl: $repoUrl")
                        viewModel.dispatch(LabAction.SubmitRepoUrl(repoUrl, desc))
                    }
                    .setNegativeButton("取消"){_, _ -> }.create()
                dialog.show()
                val onColor = ContextCompat.getColor(this@LabActivity, R.color.prim_on_color)
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(onColor)
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(onColor)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finishAfterTransition()
            R.id.lab_feedback -> showFeedback()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {


        val random = Random()

        fun start(ctx: Context, task: Task.TaskSubject) {
            val intent = Intent(ctx, LabActivity::class.java)
                .putExtra("task_id", task.id)
                .putExtra("task_title", task.title)
                .putExtra("task_img", task.imageUrl)
                .putExtra("task_date", task.updateTime)
                .putExtra("task_summary", task.summary)
            if (ctx is Activity) {
                ctx.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ctx).toBundle())
            } else {
                ctx.startActivity(intent)
            }
        }
    }

}