package cn.skygard.happyoj.intent.vm

import android.util.Log
import androidx.lifecycle.viewModelScope
import cn.skygard.common.mvi.ext.setState
import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.domain.model.SubmitLabel
import cn.skygard.happyoj.domain.model.SubmitsItem
import cn.skygard.happyoj.intent.state.CommentAction
import cn.skygard.happyoj.intent.state.CommentEvent
import cn.skygard.happyoj.intent.state.CommentState
import cn.skygard.happyoj.intent.state.FetchState
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import cn.skygard.happyoj.repo.remote.model.Comments
import cn.skygard.happyoj.repo.remote.model.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.*

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
            delay(200)
            mViewStates.setState {
                copy(comments = listOf(
                    Comments.CommitComment(
                        comment = "好！写的好！",
                        commitId = 114514,
                        createTime = "2022-10-12",
                        id = 11451419,
                        score = 100,
                        tutorName = "泡泡",
                        updateTime = "2022-10-12"
                    ),
                    Comments.CommitComment(
                        comment = "好！写的好！",
                        commitId = 114514,
                        createTime = "2022-10-12",
                        id = 11451419,
                        score = 100,
                        tutorName = "泡泡",
                        updateTime = "2022-10-12"),
                    Comments.CommitComment(
                        comment = "好！写的好！",
                        commitId = 114514,
                        createTime = "2022-10-12",
                        id = 11451419,
                        score = 100,
                        tutorName = "泡泡",
                        updateTime = "2022-10-12")
                ))
            }
            mViewStates.setState {
                copy(submits = listOf(
                    SubmitsItem(
                        submitId = 1,
                        submitUid = 11,
                        submitTitle = "测试1",
                        checkPointNum = 2,
                        checkPointLabels = listOf(SubmitLabel.Accept, SubmitLabel.Accept),
                        submitScore = 100,
                        submitAt = Calendar.getInstance().time
                    ), SubmitsItem(
                        submitId = 4,
                        submitUid = 13,
                        submitTitle = "测试2",
                        checkPointNum = 3,
                        checkPointLabels = listOf(SubmitLabel.Accept, SubmitLabel.Accept, SubmitLabel.Accept),
                        submitScore = 100,
                        submitAt = Calendar.getInstance().time
                    ), SubmitsItem(
                        submitId = 5,
                        submitUid = 12,
                        submitTitle = "测试3",
                        checkPointNum = 6,
                        checkPointLabels = listOf(
                            SubmitLabel.Accept, SubmitLabel.Accept, SubmitLabel.TimeLimitError,
                            SubmitLabel.Accept, SubmitLabel.Accept, SubmitLabel.Accept),
                        submitScore = 80,
                        submitAt = Calendar.getInstance().time
                    )
                ))
            }
//            try {
//                val commit = RetrofitHelper.taskService.getTaskCommit(tid = taskId)
//                if (commit.ok) {
//                    val comment =
//                        RetrofitHelper.taskService.getTaskComment(commit.data.commitIndex.id)
//                    if (comment.ok && comment.data.commitComment != null) {
//                        mViewStates.setState {
//                            copy(comments = comment.data.commitComment)
//                        }
//                    }
//                }
//            }catch (e: Exception) {
//                if (e is HttpException) {
//                    Result.onError(e)
//                    mViewStates.setState {
//                        copy(fetchState = FetchState.NotFetched)
//                    }
//                }
//            }
        }
    }

}