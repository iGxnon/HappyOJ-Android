package cn.skygard.happyoj.intent.state

import cn.skygard.happyoj.repo.remote.model.Task

data class SearchState(
    // for SearchFragment
    val fetchState: FetchState = FetchState.Fetched,
    val result: List<Task.TaskSubject> = emptyList(),

    // for SearchHistoryFragment
    val history: List<String> = emptyList(),

    // for SearchActivity
    val searchText: String = "",
)

sealed class SearchAction {
    object GetHistory : SearchAction()
    data class DeleteHistory(val text: String) : SearchAction()
    data class UseHistory(val text: String) : SearchAction()
    data class SetSearchText(val text: String) : SearchAction()
    data class SearchFor(val text: String) : SearchAction()
    data class QuickSearchFor(val text: String) : SearchAction()
}

sealed class SearchEvent {
    object ReplaceSearch : SearchEvent()
    object ReplaceHistory: SearchEvent()
    object StartSearch : SearchEvent()
}
