package cn.skygard.happyoj.intent.state

import cn.skygard.happyoj.repo.model.SubmitsItem

data class SubmitsState(
    val fetchState: FetchState = FetchState.Fetched,
    val submits: List<SubmitsItem> = emptyList()
)

sealed class SubmitsAction {
    object OnSwipeRefresh : SubmitsAction()
}