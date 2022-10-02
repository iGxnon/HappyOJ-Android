package cn.skygard.happyoj.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.mvi.BaseVmBindFragment
import cn.skygard.common.mvi.ext.observeEvent
import cn.skygard.common.mvi.ext.observeState
import cn.skygard.happyoj.databinding.FragmentTasksBinding
import cn.skygard.happyoj.domain.logic.UserManager
import cn.skygard.happyoj.domain.model.User
import cn.skygard.happyoj.intent.state.*
import cn.skygard.happyoj.intent.vm.MainViewModel
import cn.skygard.happyoj.intent.vm.TasksViewModel
import cn.skygard.happyoj.view.activity.CommentActivity
import cn.skygard.happyoj.view.activity.LabActivity
import cn.skygard.happyoj.view.adapter.TasksPagingAdapter
import kotlinx.coroutines.launch

class TasksFragment : BaseVmBindFragment<TasksViewModel, FragmentTasksBinding>() {

    private val parentViewModel by activityViewModels<MainViewModel>()
    private val tasksAdapter by lazyUnlock {
        TasksPagingAdapter(onClick = { item ->
            LabActivity.start(requireContext(), item)
        }, onClickCard = { tid, title ->
            CommentActivity.start(requireContext(), tid, title)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initViewState()
        initViewEvents()
    }

    private fun initView() {
        binding.rvTasks.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tasksAdapter
        }
        tasksAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> binding.srlTasks.isRefreshing = true
                is LoadState.NotLoading -> binding.srlTasks.isRefreshing = false
                is LoadState.Error -> {
                    binding.srlTasks.isRefreshing = false
                    if (UserManager.checkLogin()) {
                        "加载失败".toast()
                    }
                }
            }
        }
        binding.srlTasks.setOnRefreshListener {
//            binding.rvTasks.swapAdapter(tasksAdapter, true)
            if (UserManager.checkLogin()) {
                tasksAdapter.refresh()
            } else {
                "请登录后获取实验".toast()
                binding.srlTasks.isRefreshing = false
            }
        }
        if (UserManager.checkLogin()) {
            tasksAdapter.refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        if (UserManager.checkLogin()) {
            tasksAdapter.refresh()
        }
    }

    private fun initViewState() {
        viewModel.viewStates.run {
            observeState(this@TasksFragment, TasksState::tasksPaging) {
                viewLifecycleScope.launch {
                    tasksAdapter.submitData(it)
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
        fun newInstance(): TasksFragment {
            return TasksFragment()
        }
    }

}