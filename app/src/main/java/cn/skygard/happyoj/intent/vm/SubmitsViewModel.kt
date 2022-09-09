package cn.skygard.happyoj.intent.vm

import android.util.Log
import androidx.lifecycle.viewModelScope
import cn.skygard.common.mvi.ext.setState
import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.intent.state.FetchState
import cn.skygard.happyoj.intent.state.MainSharedEvent
import cn.skygard.happyoj.intent.state.SubmitsAction
import cn.skygard.happyoj.intent.state.SubmitsState
import cn.skygard.happyoj.repo.model.SubmitLabel
import cn.skygard.happyoj.repo.model.SubmitsItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class SubmitsViewModel :
    BaseViewModel<SubmitsState, SubmitsAction, MainSharedEvent>(SubmitsState()) {

    override fun dispatch(action: SubmitsAction) {
        Log.d("SubmitsViewModel", "received an action $action")
        when (action) {
            is SubmitsAction.OnSwipeRefresh -> fetchSubmits()
            is SubmitsAction.ItemClicked -> {

            }
        }
    }

    private fun fetchSubmits() {
        mViewStates.setState {
            copy(fetchState = FetchState.Fetching)
        }
        viewModelScope.launch {
            delay(200)
            val submits = listOf(
                SubmitsItem(
                    submitId = 10,
                    submitUid = 10,
                    submitAt = Calendar.getInstance().time,
                    submitLabel = SubmitLabel.Accept,
                    checkPointLabels = listOf(SubmitLabel.Accept, SubmitLabel.Accept),
                    checkPointNum = 2,
                    submitScore = 100,
                ),
                SubmitsItem(
                    submitId = 12,
                    submitUid = 12,
                    submitAt = Calendar.getInstance().time,
                    submitLabel = SubmitLabel.RuntimeError,
                    checkPointLabels = listOf(SubmitLabel.RuntimeError),
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