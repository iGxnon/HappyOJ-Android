package cn.skygard.happyoj.intent.state

import cn.skygard.happyoj.domain.model.SubmitsItem
import cn.skygard.happyoj.repo.remote.model.Comments

data class CommentState(
    val fetchState: FetchState = FetchState.Fetched,
    val comments: List<Comments.CommitComment> = emptyList(),
    val submits: List<SubmitsItem> = emptyList()
)

sealed class CommentAction {
    object GetData: CommentAction()
}

sealed class CommentEvent