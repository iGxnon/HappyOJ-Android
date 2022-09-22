package cn.skygard.happyoj.intent.vm

import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.intent.state.LoginAction
import cn.skygard.happyoj.intent.state.LoginEvent
import cn.skygard.happyoj.intent.state.LoginState

class LoginViewModel : BaseViewModel<LoginState, LoginAction, LoginEvent>(LoginState()) {
    override fun dispatch(action: LoginAction) {
        TODO("Not yet implemented")
    }
}