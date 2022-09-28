package cn.skygard.happyoj.intent.vm

import android.util.Log
import androidx.lifecycle.viewModelScope
import cn.skygard.common.mvi.ext.setState
import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.intent.state.CommentAction
import cn.skygard.happyoj.intent.state.CommentEvent
import cn.skygard.happyoj.intent.state.CommentState
import cn.skygard.happyoj.intent.state.FetchState
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import cn.skygard.happyoj.repo.remote.model.Result
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CommentViewModel(val taskId: Long) :
    BaseViewModel<CommentState, CommentAction, CommentEvent>(CommentState()) {

    override fun dispatch(action: CommentAction) {
        Log.d("CommentViewModel", "received an action $action")
        when (action) {
            CommentAction.GetData -> getData()
        }
    }

    private fun getData() {
        viewModelScope.launch {
            try {
                val commit = RetrofitHelper.taskService.getTaskCommit(tid = taskId)
                if (commit.ok) {
                    val comment =
                        RetrofitHelper.taskService.getTaskComment(commit.data.commitIndex.id)
                    if (comment.ok && comment.data.commitComment != null) {
                        mViewStates.setState {
                            copy(comments = comment.data.commitComment)
                        }
                    }
                }
            }catch (e: Exception) {
                if (e is HttpException) {
                    Result.onError(e)
                    mViewStates.setState {
                        copy(fetchState = FetchState.NotFetched)
                    }
                }
            }
        }
    }

}