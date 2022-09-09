package cn.skygard.happyoj.intent.vm

import android.util.Log
import androidx.lifecycle.viewModelScope
import cn.skygard.common.mvi.ext.setState
import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.intent.state.FetchState
import cn.skygard.happyoj.intent.state.LabAction
import cn.skygard.happyoj.intent.state.LabEvent
import cn.skygard.happyoj.intent.state.LabState
import cn.skygard.happyoj.repo.model.TasksItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LabViewModel(val taskItem: TasksItem) :
    BaseViewModel<LabState, LabAction, LabEvent>(LabState()) {

    override fun dispatch(action: LabAction) {
        Log.d("LabViewModel", "received an action $action")
        when (action) {
            is LabAction.FetchContent -> fetchContent(action.noCache)
        }
    }

    private fun fetchContent(noCache: Boolean) {
        Log.d("LabViewModel", "fetching data from ${taskItem.mdUrl}")
        mViewStates.setState {
            copy(fetchState = FetchState.Fetching)
        }
        viewModelScope.launch {
            delay(1000)
            mViewStates.setState {
                copy(
                    fetchState = FetchState.Fetched,
                    mdContent = """
                        # Begin

                        > 啊，一个新的世界！
                        >
                        > 我是这个博客的主人，`iGxnon`(~~我也不会读~~)，中文常用的就是  `泡泡`，这是第一篇博文
                        >
                        > 我将会更新一些 个人项目，学习打卡，和一些好玩的东西
                    """.trimIndent()
                )
            }
        }
    }

}