package cn.skygard.happyoj.intent.vm

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cn.skygard.common.mvi.ext.setState
import cn.skygard.common.mvi.ext.triggerEvent
import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.intent.state.*
import cn.skygard.happyoj.repo.pagingsource.TasksPagingSource
import cn.skygard.happyoj.repo.remote.model.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TasksViewModel :
    BaseViewModel<TasksState, TasksAction, MainSharedEvent>(TasksState()) {

    private val pagingData by lazy {
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20,
            ),
            pagingSourceFactory = {
                TasksPagingSource()
            }
        ).flow.catch { e ->
            Result.onError(e.cause)
        }.cachedIn(viewModelScope)
    }

    init {
        viewModelScope.launch {
            pagingData.collectLatest {
                mViewStates.setState {
                    copy(tasksPaging = it)
                }
            }
        }
    }

    override fun dispatch(action: TasksAction) {
        Log.d("TasksViewModel", "received an action $action")
        when (action) {
            is TasksAction.AddToFavor -> {
                mViewEvents.triggerEvent(MainSharedEvent.ShowSnack("已经添加进收藏", "取消"))
            }
        }
    }

}