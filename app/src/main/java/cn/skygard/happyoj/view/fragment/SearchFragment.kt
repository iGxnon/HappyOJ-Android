package cn.skygard.happyoj.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.base.ui.BaseBindFragment
import cn.skygard.happyoj.databinding.FragmentSearchBinding
import cn.skygard.happyoj.intent.vm.SearchViewModel
import cn.skygard.happyoj.view.activity.LabActivity
import cn.skygard.happyoj.view.adapter.TasksRvAdapter

class SearchFragment : BaseBindFragment<FragmentSearchBinding>() {

    private val viewModel by activityViewModels<SearchViewModel>()
    private val rvAdapter by lazyUnlock {
        TasksRvAdapter {
            LabActivity.start(requireContext(), this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {
        binding.rvSearch.run {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
        fun newInstance() = SearchFragment()
    }


}