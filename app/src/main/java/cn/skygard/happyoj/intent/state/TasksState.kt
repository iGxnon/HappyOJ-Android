package cn.skygard.happyoj.intent.state

import cn.skygard.happyoj.domain.model.TasksItem

data class TasksState (
    val fetchState: FetchState = FetchState.Fetched,
    val tasks: List<TasksItem> = emptyList()
)

sealed class TasksAction {
    object OnSwipeRefresh : TasksAction()
    data class AddToFavor(val item: TasksItem) : TasksAction()
}

enum class FetchState {
    Fetching,
    Fetched,
    NotFetched
}
