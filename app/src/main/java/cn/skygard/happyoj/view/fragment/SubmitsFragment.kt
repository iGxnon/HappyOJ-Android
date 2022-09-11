package cn.skygard.happyoj.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.mvi.BaseVmBindFragment
import cn.skygard.common.mvi.ext.observeEvent
import cn.skygard.common.mvi.ext.observeState
import cn.skygard.happyoj.databinding.FragmentSubmitsBinding
import cn.skygard.happyoj.intent.state.*
import cn.skygard.happyoj.intent.vm.MainViewModel
import cn.skygard.happyoj.intent.vm.SubmitsViewModel
import cn.skygard.happyoj.view.adapter.SubmitsRvAdapter

class SubmitsFragment : BaseVmBindFragment<SubmitsViewModel, FragmentSubmitsBinding>() {

    private val parentViewModel by activityViewModels<MainViewModel>()
    private val submitsRvAdapter by lazyUnlock {
        SubmitsRvAdapter ()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initViewState()
        initViewEvents()
    }

    private fun initView() {
        viewModel.dispatch(SubmitsAction.OnSwipeRefresh)
        binding.rvSubmits.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = submitsRvAdapter
        }
        binding.srlSubmits.setOnRefreshListener {
            viewModel.dispatch(SubmitsAction.OnSwipeRefresh)
        }
    }

    private fun initViewState() {
        viewModel.viewStates.run {
            observeState(this@SubmitsFragment, SubmitsState::submits) {
                submitsRvAdapter.submitList(it)
            }
            observeState(this@SubmitsFragment, SubmitsState::fetchState) {
                when (it) {
                    FetchState.Fetching -> {
                        binding.srlSubmits.isRefreshing = true
                    }
                    FetchState.Fetched -> {
                        binding.srlSubmits.isRefreshing = false
                    }
                    FetchState.NotFetched -> {
                        binding.srlSubmits.isRefreshing = false
                        toast("Load failed!")
                    }
                }
            }
        }
    }

    private fun initViewEvents() {
        viewModel.viewEvents.observeEvent(this) {
            parentViewModel.dispatch(MainAction.TriggerSharedEvent(it))
        }
    }

    companion object {
        fun newInstance() = SubmitsFragment()
    }
}