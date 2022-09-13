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
                SubmitsItem(
                    submitId = 10,
                    submitUid = 10,
                    submitTitle = "实验一：114514",
                    submitAt = Calendar.getInstance().time,
                    checkPointLabels = listOf(SubmitLabel.Accept, SubmitLabel.Accept),
                    checkPointNum = 2,
                    submitScore = 100,
                ),
                SubmitsItem(
                    submitId = 12,
                    submitUid = 12,
                    submitTitle = "实验一：1919810",
                    submitAt = Calendar.getInstance().time,
                    checkPointLabels = listOf(SubmitLabel.RuntimeError),
                    checkPointNum = 1,
                    submitScore = 0,
                ),
                SubmitsItem(
                    submitId = 15,
                    submitUid = 15,
                    submitTitle = "实验一：1145141919810",
                    submitAt = Calendar.getInstance().time,
                    checkPointLabels = listOf(
                        SubmitLabel.TimeLimitError,
                        SubmitLabel.Accept, SubmitLabel.Accept,
                        SubmitLabel.MemoryLimitError,
                        SubmitLabel.Accept, SubmitLabel.Accept,
                    ),
                    checkPointNum = 6,
                    submitScore = 80,
                ),
                SubmitsItem(
                    submitId = 16,
                    submitUid = 16,
                    submitTitle = "实验一：一个一个一个",
                    submitAt = Calendar.getInstance().time,
                    checkPointLabels = listOf(SubmitLabel.WrongAnswer),
                    checkPointNum = 1,
                    submitScore = 0,
                ),
            )
            mViewStates.setState {
                copy(fetchState = FetchState.Fetched, submits = submits)
            }
        }
    }

}