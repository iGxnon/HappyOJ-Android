package cn.skygard.happyoj.intent.state

data class LabState(
    val currentPage: Pages = Pages.LAB,
    val menuVisibility: Boolean = true,
    val contentFetchState: FetchState = FetchState.Fetched,
    val mdContent: String = ""
)

sealed class LabAction{
    object HideMenu : LabAction()
    object ShowMenu : LabAction()
    data class ChangePage(val page: Pages) : LabAction()
    data class FetchContent(val noCache: Boolean = false) : LabAction()
    object ScrollToTop : LabAction()
}

sealed class LabEvent {
    object ScrollToTop : LabEvent()
}

enum class Pages(val title: String, val index: Int){
    LAB("LAB", 0), COMMIT("COMMIT", 1);

    companion object {
        fun all(): List<String> {
            return listOf(LAB.title, COMMIT.title)
        }
    }
}