package cn.skygard.happyoj.intent.vm

import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.intent.state.MainAction
import cn.skygard.happyoj.intent.state.MainEvent
import cn.skygard.happyoj.intent.state.MainState

class MainViewModel : BaseViewModel<MainState, MainAction, MainEvent>() {

    override fun dispatch(action: MainAction) {
        TODO("Not yet implemented")
    }
}