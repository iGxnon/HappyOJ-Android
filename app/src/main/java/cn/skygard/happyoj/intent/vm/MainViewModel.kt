package cn.skygard.happyoj.intent.vm

import android.util.Log
import cn.skygard.common.mvi.ext.triggerEvent
import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.intent.state.MainAction
import cn.skygard.happyoj.intent.state.MainSharedEvent
import cn.skygard.happyoj.intent.state.MainState

class MainViewModel :
    BaseViewModel<MainState, MainAction, MainSharedEvent>(MainState()) {

    override fun dispatch(action: MainAction) {
        Log.d("MainViewModel", "received an action $action")
        when (action) {
            is MainAction.TriggerSharedEvent -> {
                mViewEvents.triggerEvent(action.event)
            }
        }
    }

}