package cn.skygard.happyoj.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.base.ui.BaseBindFragment
import cn.skygard.happyoj.databinding.FragmentSearchHistoryBinding
import cn.skygard.happyoj.intent.vm.SearchViewModel
import cn.skygard.happyoj.view.adapter.SearchHistoryRvAdapter


class SearchHistoryFragment : BaseBindFragment<FragmentSearchHistoryBinding>() {

    private val viewModel by activityViewModels<SearchViewModel>()
    private val searchHistoryRvAdapter by lazyUnlock {
        SearchHistoryRvAdapter {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {

    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchHistoryFragment()
    }


}