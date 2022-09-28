package cn.skygard.happyoj.intent.state

import cn.skygard.happyoj.repo.remote.model.Task

data class SubmitsState(
    val fetchState: FetchState = FetchState.Fetched,
    val submits: List<Task.TaskSubject> = emptyList()
)

sealed class SubmitsAction {
    object OnSwipeRefresh : SubmitsAction()
}