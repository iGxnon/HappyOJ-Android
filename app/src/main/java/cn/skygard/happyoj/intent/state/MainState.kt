package cn.skygard.happyoj.intent.state

import android.view.View

class MainState

sealed class MainSharedEvent {
    data class ShowSnack(
        val mess: String,
        val action: String = "",
        val actionCallback: View.OnClickListener = View.OnClickListener {  }
    ) : MainSharedEvent()
}

sealed class MainAction {
    data class TriggerSharedEvent(val event: MainSharedEvent) : MainAction()
    object RefreshData: MainAction()
}