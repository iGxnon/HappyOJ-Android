package cn.skygard.happyoj.intent.state

data class LabState (
    val fetchState: FetchState = FetchState.Fetched,
    val mdContent: String = ""
)

sealed class LabEvent

sealed class LabAction {
    data class FetchContent(val noCache: Boolean = false) : LabAction()
}