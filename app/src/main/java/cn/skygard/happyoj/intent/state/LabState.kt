package cn.skygard.happyoj.intent.state

import androidx.paging.PagingData
import cn.skygard.happyoj.repo.remote.model.RepoCommit

data class LabState(
    val currentPage: Pages = Pages.LAB,
    val menuVisibility: Boolean = true,
    val contentFetchState: FetchState = FetchState.Fetched,
    val mdContent: String = "",
    val repoCommitPaging: PagingData<RepoCommit> = PagingData.empty()
)

sealed class LabAction{
    object HideMenu : LabAction()
    object ShowMenu : LabAction()
    object RepoCommitRefresh : LabAction()
    data class ChangePage(val page: Pages) : LabAction()
    data class FetchContent(val noCache: Boolean = false) : LabAction()
    object ScrollToTop : LabAction()
    data class SubmitRepoUrl(val url: String, val desc: String = "") : LabAction()
}

sealed class LabEvent {
    object ScrollToTop : LabEvent()
    object SubmitSuccess: LabEvent()
    object SubmitFailed: LabEvent()
    data class TriggerRefreshRepoCommit(val isRefresh: Boolean) : LabEvent()
}

enum class Pages(val title: String, val index: Int){
    LAB("LAB", 0), COMMIT("COMMIT", 1);

    companion object {
        fun all(): List<String> {
            return listOf(LAB.title, COMMIT.title)
        }
    }
}