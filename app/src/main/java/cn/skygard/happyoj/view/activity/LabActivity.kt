package cn.skygard.happyoj.view.activity

import android.animation.ValueAnimator
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
import android.view.*
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.skygard.common.base.BaseApp
import cn.skygard.common.base.ext.color
import cn.skygard.common.base.ext.gone
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.base.ext.visible
import cn.skygard.common.mvi.BaseVmBindActivity
import cn.skygard.common.mvi.ext.observeState
import cn.skygard.happyoj.R
import cn.skygard.happyoj.databinding.ActivityLabBinding
import cn.skygard.happyoj.databinding.DialogFeedbackBinding
import cn.skygard.happyoj.intent.state.FetchState
import cn.skygard.happyoj.intent.state.LabAction
import cn.skygard.happyoj.intent.state.LabState
import cn.skygard.happyoj.intent.vm.LabViewModel
import cn.skygard.happyoj.repo.model.TasksItem
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.glide.GlideImagesPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import java.lang.IllegalArgumentException
import java.util.*
import java.util.regex.Pattern


class LabActivity : BaseVmBindActivity<LabViewModel, ActivityLabBinding>() {

    override val isCancelStatusBar: Boolean
        get() = true

    private val markdon by lazyUnlock {
        Markwon.builder(this) // automatically create Glide instance
            .usePlugin(MarkwonInlineParserPlugin.create())
            .usePlugin(GlideImagesPlugin.create(Glide.with(this))) // use supplied Glide instance
            .usePlugin(HtmlPlugin.create())
            .usePlugin(JLatexMathPlugin.create(binding.tvContent.textSize) {
                it.inlinesEnabled(true)
            })
            .build()
    }


    private val taskItem by lazyUnlock {
        TasksItem(
            taskId = intent.getIntExtra("task_id", -1),
            title = intent.getStringExtra("task_title")!!,
            imageUrl = intent.getStringExtra("task_img")!!,
            mdUrl = intent.getStringExtra("task_content")!!,
            date = intent.getSerializableExtra("task_date")!! as Date,
            shortcut = intent.getStringExtra("task_shortcut")!!
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
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        super.onCreate(savedInstanceState)
        initAnim()
        initView()
        initViewStates()
    }

    private fun initAnim() {
        val transitionSet = TransitionSet()
        transitionSet.addTransition(ChangeBounds())
        transitionSet.addTransition(ChangeClipBounds())
        transitionSet.addTransition(ChangeImageTransform())
        window.sharedElementEnterTransition = transitionSet
        window.sharedElementExitTransition = transitionSet
        binding.tvDesc.transitionName = intent.getStringExtra(TransitionNameDesc)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.lab_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initViewStates() {
        viewModel.viewStates.run {
            observeState(this@LabActivity, LabState::mdContent) {
                markdon.setMarkdown(binding.tvContent, it)
            }
            observeState(this@LabActivity, LabState::fetchState) {
                when (it) {
                    FetchState.Fetching -> {
                        binding.tvContent.gone()
                        binding.pbContent.visible()
                    }
                    FetchState.Fetched -> {
                        binding.pbContent.gone()
                        binding.tvContent.visible()
                    }
                    FetchState.NotFetched -> {
                        binding.pbContent.gone()
                        binding.tvContent.visible()
                        binding.tvLoadFail.visible()
                    }
                }
            }
        }
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
            tvDesc.text = taskItem.shortcut
            if (taskItem.imageUrl != "") {
                Glide.with(this@LabActivity)
                    .load(taskItem.imageUrl)
                    .into(ivBackground)
            }
            ivBgTint.setImageResource(randomTint())
            randomTint().color.run {
                fabFavor.colorNormal = this
                fabFavor.colorPressed = this
            }
            randomTint().color.run {
                fabMenu.menuButtonColorNormal = this
                fabMenu.menuButtonColorPressed = this
            }
            randomTint().color.run {
                fabRefresh.colorNormal = this
                fabRefresh.colorPressed = this
            }
            randomTint().color.run {
                fabTop.colorNormal = this
                fabTop.colorPressed = this
            }
            fabRefresh.setOnClickListener {
                viewModel.dispatch(LabAction.FetchContent(true))
            }
            nvContent.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener {
                    _, _, scrollY, _, oldScrollY ->
                if (scrollY > oldScrollY)
                    fabMenu.hideMenuButton(true)
                else if (scrollY < oldScrollY)
                    fabMenu.showMenuButton(true)
            })
            fabTop.setOnClickListener {
                ValueAnimator.ofInt(nvContent.scrollY, 0).run {
                    interpolator = AccelerateInterpolator()
                    addUpdateListener {
                        nvContent.scrollY = it.animatedValue as Int
                    }
                    start()
                }
            }
        }
        viewModel.dispatch(LabAction.FetchContent())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finishAfterTransition()
            R.id.lab_feedback -> {
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
                        Snackbar.make(binding.root, "提交成功", Snackbar.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("取消"){_, _ -> }.create()
                dialog.show()
                val onColor = ContextCompat.getColor(this, R.color.prim_on_color)
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(onColor)
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(onColor)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        @Deprecated("废弃")
        private const val TransitionNameHeader = "transition_name_header"
        private const val TransitionNameDesc = "transition_name_desc"

        private val random = Random()

        fun start(ctx: Context, task: TasksItem,
                  titleView: View, transitionNameHeader: String,
                  descView: View, transitionNameDesc: String) {
            val intent = Intent(ctx, LabActivity::class.java)
                .putExtra("task_id", task.taskId)
                .putExtra("task_title", task.title)
                .putExtra("task_img", task.imageUrl)
                .putExtra("task_content", task.mdUrl)
                .putExtra("task_date", task.date)
                .putExtra("task_shortcut", task.shortcut)
                .putExtra(TransitionNameHeader, transitionNameHeader)
                .putExtra(TransitionNameDesc, transitionNameDesc)
            if (ctx is Activity) {
                ctx.startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(ctx,
                        Pair.create(titleView, transitionNameHeader),
                        Pair.create(descView, transitionNameDesc)).toBundle())
            } else {
                ctx.startActivity(intent)
            }
        }
    }

}