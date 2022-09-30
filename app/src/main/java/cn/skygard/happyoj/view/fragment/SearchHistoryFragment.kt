package cn.skygard.happyoj.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.base.ui.BaseBindFragment
import cn.skygard.common.mvi.ext.observeState
import cn.skygard.happyoj.databinding.FragmentSearchHistoryBinding
import cn.skygard.happyoj.intent.state.SearchAction
import cn.skygard.happyoj.intent.state.SearchState
import cn.skygard.happyoj.intent.vm.SearchViewModel
import cn.skygard.happyoj.view.adapter.SearchHistoryRvAdapter


class SearchHistoryFragment : BaseBindFragment<FragmentSearchHistoryBinding>() {

    private val viewModel by activityViewModels<SearchViewModel>()
    private val searchHistoryRvAdapter by lazyUnlock {
        SearchHistoryRvAdapter(onClick = {
            viewModel.dispatch(SearchAction.SetSearchText(this))
        }, onDelete =  {
            viewModel.dispatch(SearchAction.DeleteHistory(it))
            viewModel.dispatch(SearchAction.GetHistory)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initViewState()
    }

    private fun initView() {
        binding.rvSearchHistory.run {
            adapter = searchHistoryRvAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.dispatch(SearchAction.GetHistory)
    }

    private fun initViewState() {
        viewModel.viewStates.run {
            observeState(this@SearchHistoryFragment, SearchState::history) {
                // 只取前六个
                searchHistoryRvAdapter.submitList(it.take(6))
            }
        }
    }

    companion object {
        fun newInstance() = SearchHistoryFragment()
    }


}