package cn.skygard.happyoj.intent.state

import androidx.paging.PagingData
import cn.skygard.happyoj.repo.remote.model.Task

data class TasksState (
    val tasksPaging: PagingData<Task.TaskSubject> = PagingData.empty()
)

sealed class TasksAction {
    data class AddToFavor(val item: Task.TaskSubject) : TasksAction()
}

enum class FetchState {
    Fetching,
    Fetched,
    NotFetched
}
