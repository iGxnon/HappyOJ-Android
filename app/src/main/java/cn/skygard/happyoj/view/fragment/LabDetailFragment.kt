package cn.skygard.happyoj.view.fragment

import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.activityViewModels
import cn.skygard.common.base.ext.gone
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.base.ext.visible
import cn.skygard.common.base.ui.BaseBindFragment
import cn.skygard.common.mvi.ext.observeEvent
import cn.skygard.common.mvi.ext.observeState
import cn.skygard.happyoj.databinding.FragmentLabDetailBinding
import cn.skygard.happyoj.intent.state.*
import cn.skygard.happyoj.intent.vm.LabViewModel
import com.bumptech.glide.Glide
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.glide.GlideImagesPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import kotlinx.coroutines.launch

class LabDetailFragment :
    BaseBindFragment<FragmentLabDetailBinding>() {

    private val markdown by lazyUnlock {
        Markwon.builder(requireContext())
            .usePlugin(MarkwonInlineParserPlugin.create())
            .usePlugin(GlideImagesPlugin.create(Glide.with(this)))
            .usePlugin(HtmlPlugin.create())
            .build()
    }

    private val viewModel by activityViewModels<LabViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initViewStates()
        initViewEvents()
    }

    private fun initView() {
        binding.run {
            srlTask.setOnRefreshListener {
                viewModel.dispatch(LabAction.FetchContent(true))
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                nvContent.setOnScrollChangeListener {
                        _, _, scrollY, _, oldScrollY ->
                    if (scrollY > oldScrollY) {
                        Log.d("LabDetailFragment", "hide menu")
                        viewModel.dispatch(LabAction.HideMenu)
                    }
                    else if (scrollY < oldScrollY) {
                        viewModel.dispatch(LabAction.ShowMenu)
                    }
                }
            }
        }
        viewModel.dispatch(LabAction.FetchContent(false))
    }

    private fun initViewStates() {
        viewModel.viewStates.run {
            observeState(this@LabDetailFragment, LabState::mdContent) {
                viewLifecycleScope.launch {
                    markdown.setMarkdown(binding.tvContent, it)
                }
            }
            observeState(this@LabDetailFragment, LabState::contentFetchState) {
                when (it) {
                    FetchState.Fetching -> {
                        binding.tvContent.gone()
                        binding.srlTask.isRefreshing = true
                    }
                    FetchState.Fetched -> {
                        binding.srlTask.isRefreshing = false
                        binding.tvContent.visible()
                    }
                    FetchState.NotFetched -> {
                        binding.srlTask.isRefreshing = false
                        binding.tvContent.visible()
                    }
                }
            }
        }
    }

    private fun initViewEvents() {
        viewModel.viewEvents.run {
            observeEvent(this@LabDetailFragment) {
                when (it) {
                    LabEvent.ScrollToTop -> {
                        ValueAnimator.ofInt(binding.nvContent.scrollY, 0).run {
                            interpolator = AccelerateInterpolator()
                            addUpdateListener { anim ->
                                binding.nvContent.scrollY = anim.animatedValue as Int
                            }
                            start()
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    companion object {
        fun newInstance() = LabDetailFragment()
    }

}