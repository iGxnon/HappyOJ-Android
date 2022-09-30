package cn.skygard.happyoj.view.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.base.ui.BaseBindFragment
import cn.skygard.common.mvi.ext.observeEvent
import cn.skygard.common.mvi.ext.observeState
import cn.skygard.happyoj.databinding.FragmentLabCommitBinding
import cn.skygard.happyoj.domain.logic.UserManager
import cn.skygard.happyoj.intent.state.LabAction
import cn.skygard.happyoj.intent.state.LabEvent
import cn.skygard.happyoj.intent.state.LabState
import cn.skygard.happyoj.intent.vm.LabViewModel
import cn.skygard.happyoj.view.adapter.RepoCommitPagingAdapter
import kotlinx.coroutines.launch

class LabCommitFragment : BaseBindFragment<FragmentLabCommitBinding>() {

    private val commitAdapter by lazyUnlock {
        RepoCommitPagingAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initViewStates()
        initViewEvents()
    }

    private val viewModel by activityViewModels<LabViewModel>()

    private fun initView() {
        binding.run {
            rvCommit.run {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = commitAdapter
            }
            srlCommit.setOnRefreshListener {
                viewModel.dispatch(LabAction.RepoCommitRefresh)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rvCommit.setOnScrollChangeListener {
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
        commitAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> binding.srlCommit.isRefreshing = true
                is LoadState.NotLoading -> binding.srlCommit.isRefreshing = false
                is LoadState.Error -> {
                    binding.srlCommit.isRefreshing = false
                    if (UserManager.checkLogin()) {
                        "加载失败".toast()
                    }
                }
            }
        }
    }

    private fun initViewStates() {
        viewModel.viewStates.run {
            observeState(this@LabCommitFragment, LabState::repoCommitPaging) {
                viewLifecycleScope.launch {
                    commitAdapter.submitData(it)
                }
            }
        }
    }

    private fun initViewEvents() {
        viewModel.viewEvents.run {
            observeEvent(this@LabCommitFragment) {
                when (it) {
                    is LabEvent.ScrollToTop -> {
                        Log.d("LabCommitFragment", "ScrollToTop")
                        binding.rvCommit.scrollToPosition(0)
                    }
                    is LabEvent.TriggerRefreshRepoCommit -> {
                        if (it.isRefresh) {
                            commitAdapter.refresh()
                        } else {
                            "请上传仓库".toast()
                            binding.srlCommit.isRefreshing = false
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    companion object {
        fun newInstance() = LabCommitFragment()
    }

}