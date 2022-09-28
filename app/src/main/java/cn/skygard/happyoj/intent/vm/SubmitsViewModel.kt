package cn.skygard.happyoj.intent.vm

import android.util.Log
import androidx.lifecycle.viewModelScope
import cn.skygard.common.mvi.ext.setState
import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.intent.state.FetchState
import cn.skygard.happyoj.intent.state.MainSharedEvent
import cn.skygard.happyoj.intent.state.SubmitsAction
import cn.skygard.happyoj.intent.state.SubmitsState
import cn.skygard.happyoj.domain.model.SubmitLabel
import cn.skygard.happyoj.domain.model.SubmitsItem
import cn.skygard.happyoj.repo.remote.model.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class SubmitsViewModel :
    BaseViewModel<SubmitsState, SubmitsAction, MainSharedEvent>(SubmitsState()) {

    override fun dispatch(action: SubmitsAction) {
        Log.d("SubmitsViewModel", "received an action $action")
        when (action) {
            is SubmitsAction.OnSwipeRefresh -> fetchSubmits()
        }
    }

    private fun fetchSubmits() {
        mViewStates.setState {
            copy(fetchState = FetchState.Fetching)
        }
        viewModelScope.launch {
            delay(300)
            val submits = listOf(
                Task.TaskSubject(
                    attr = 0,
                    id = -1,
                    imageUrl = "https://img.skygard.cn/mmexport1650686453581.jpg",
                    state = 0,
                    summary = "介绍xxxxx",
                    title = "标题xxxx",
                    updateTime = "2021-20-33 dawidjiaw"
                )
            )
            mViewStates.setState {
                copy(fetchState = FetchState.Fetched, submits = submits)
            }
        }
    }

}