package cn.skygard.happyoj.intent.state

import cn.skygard.happyoj.repo.model.TasksItem

data class TasksState (
    val fetchState: FetchState = FetchState.Fetched,
    val tasks: List<TasksItem> = emptyList()
)

sealed class TasksAction {
    data class ItemClicked(val item: TasksItem) : TasksAction()
    object OnSwipeRefresh : TasksAction()
    data class AddToFavor(val item: TasksItem) : TasksAction()
}

enum class FetchState {
    Fetching,
    Fetched,
    NotFetched
}
