package cn.skygard.happyoj.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import cn.skygard.common.base.ui.BaseBindFragment
import cn.skygard.happyoj.databinding.FragmentLabCommitBinding
import cn.skygard.happyoj.intent.vm.LabViewModel

class LabCommitFragment : BaseBindFragment<FragmentLabCommitBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initViewStates()
        initViewEvents()
    }

    private val viewModel by activityViewModels<LabViewModel>()

    private fun initView() {
        binding.run {
            srlCommit.setOnRefreshListener {

            }
        }
    }

    private fun initViewStates() {

    }

    private fun initViewEvents() {

    }

    companion object {
        fun newInstance() = LabCommitFragment()
    }

}