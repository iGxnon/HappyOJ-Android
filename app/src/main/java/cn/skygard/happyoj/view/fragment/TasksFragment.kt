package cn.skygard.happyoj.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.mvi.BaseVmBindFragment
import cn.skygard.common.mvi.ext.observeEvent
import cn.skygard.common.mvi.ext.observeState
import cn.skygard.happyoj.databinding.FragmentTasksBinding
import cn.skygard.happyoj.intent.state.*
import cn.skygard.happyoj.intent.vm.MainViewModel
import cn.skygard.happyoj.intent.vm.TasksViewModel
import cn.skygard.happyoj.view.activity.LabActivity
import cn.skygard.happyoj.view.activity.MainActivity
import cn.skygard.happyoj.view.adapter.TasksRvAdapter

class TasksFragment : BaseVmBindFragment<TasksViewModel, FragmentTasksBinding>() {

    private val parentViewModel by activityViewModels<MainViewModel>()
    private val tasksRvAdapter by lazyUnlock {
        TasksRvAdapter {
            LabActivity.start(requireContext(), this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initViewState()
        initViewEvents()
    }

    private fun initView() {
        viewModel.dispatch(TasksAction.OnSwipeRefresh)
        binding.rvTasks.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tasksRvAdapter
        }
        binding.srlTasks.setOnRefreshListener {
            viewModel.dispatch(TasksAction.OnSwipeRefresh)
        }
    }

    private fun initViewState() {
        viewModel.viewStates.run {
            observeState(this@TasksFragment, TasksState::tasks) {
                tasksRvAdapter.submitList(it)
            }
            observeState(this@TasksFragment, TasksState::fetchState) {
                when (it) {
                    FetchState.Fetching -> {
                        binding.srlTasks.isRefreshing = true
                    }
                    FetchState.Fetched -> {
                        binding.srlTasks.isRefreshing = false
                    }
                    FetchState.NotFetched -> {
                        binding.srlTasks.isRefreshing = false
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
        @JvmStatic
        fun newInstance(): TasksFragment {
            return TasksFragment()
        }
    }

}