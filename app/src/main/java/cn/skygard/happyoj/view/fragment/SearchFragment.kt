package cn.skygard.happyoj.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.base.ui.BaseBindFragment
import cn.skygard.common.mvi.ext.observeState
import cn.skygard.happyoj.databinding.FragmentSearchBinding
import cn.skygard.happyoj.intent.state.FetchState
import cn.skygard.happyoj.intent.state.SearchState
import cn.skygard.happyoj.intent.vm.SearchViewModel
import cn.skygard.happyoj.view.activity.LabActivity
import cn.skygard.happyoj.view.adapter.TasksRvAdapter

class SearchFragment : BaseBindFragment<FragmentSearchBinding>() {

    private val viewModel by activityViewModels<SearchViewModel>()
    private val rvAdapter by lazyUnlock {
        TasksRvAdapter { item, headerView, transitionNameHeader, descView, transitionNameDesc ->
            LabActivity.start(requireContext(), item, headerView,
                transitionNameHeader, descView, transitionNameDesc)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initViewState()
    }

    private fun initView() {
        binding.rvSearch.run {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initViewState() {
        viewModel.viewStates.run {
            observeState(this@SearchFragment, SearchState::fetchState) {
                when (it) {
                    FetchState.Fetching -> {}
                    FetchState.Fetched -> {}
                    FetchState.NotFetched -> {}
                }
            }
            observeState(this@SearchFragment, SearchState::result) {
                rvAdapter.submitList(it)
            }
        }
    }

    companion object {
        fun newInstance() = SearchFragment()
    }


}