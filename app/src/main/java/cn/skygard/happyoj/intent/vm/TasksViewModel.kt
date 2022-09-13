package cn.skygard.happyoj.intent.vm

import android.util.Log
import androidx.lifecycle.viewModelScope
import cn.skygard.common.mvi.ext.setState
import cn.skygard.common.mvi.ext.triggerEvent
import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.intent.state.*
import cn.skygard.happyoj.domain.model.TasksItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class TasksViewModel :
    BaseViewModel<TasksState, TasksAction, MainSharedEvent>(TasksState()) {

    override fun dispatch(action: TasksAction) {
        Log.d("TasksViewModel", "received an action $action")
        when (action) {
            is TasksAction.OnSwipeRefresh -> fetchTasks()
            is TasksAction.AddToFavor -> {
                mViewEvents.triggerEvent(MainSharedEvent.ShowSnack("已经添加进收藏", "取消"))
            }
        }
    }


    private fun fetchTasks() {
        mViewStates.setState {
            copy(fetchState = FetchState.Fetching)
        }
        // for test
        viewModelScope.launch {
            delay(300)
            val tasks = listOf(
                TasksItem(
                    taskId = 1,
                    title = "实验一：基础语法",
                    shortcut = "学习 Golang 的基础语法，杀马特团长，你就领那俩狗徒弟，就像那舒克和贝塔，见到你必须给你头套薅掉",
                    date = Calendar.getInstance().time,
                    mdUrl = ""
                ),
                TasksItem(
                    taskId = 2,
                    title = "实验二：接口",
                    shortcut = "使用 Golang 的接口，我徒弟呢？杀马特团长，你给我等着，你给我等着！！！",
                    imageUrl = "https://img.skygard.cn/dahuoji.jpeg",
                    date = Calendar.getInstance().time,
                    mdUrl = ""
                )
            )
            mViewStates.setState {
                copy(fetchState = FetchState.Fetched, tasks = tasks)
            }
        }
    }

}