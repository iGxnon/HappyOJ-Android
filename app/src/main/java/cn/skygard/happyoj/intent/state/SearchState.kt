package cn.skygard.happyoj.intent.state

data class SearchState(
    val fetchState: FetchState = FetchState.Fetched,
    val isSearch: Boolean = false,
)

sealed class SearchAction {
    object ShowHistory: SearchAction()
    data class SetSearchText(val text: String) : SearchAction()
    data class SearchFor(val text: String) : SearchAction()
}

