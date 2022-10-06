package cn.skygard.happyoj.intent.vm

import android.util.Log
import androidx.lifecycle.viewModelScope
import cn.skygard.common.mvi.ext.setState
import cn.skygard.common.mvi.ext.triggerEvent
import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.domain.logic.UserManager
import cn.skygard.happyoj.intent.state.LoginAction
import cn.skygard.happyoj.intent.state.LoginEvent
import cn.skygard.happyoj.intent.state.LoginState
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import cn.skygard.happyoj.repo.remote.service.UserService
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel<LoginState, LoginAction, LoginEvent>(LoginState()) {
    override fun dispatch(action: LoginAction) {
        Log.d("LoginViewModel", "received an action $action")
        when (action) {
            is LoginAction.ChangePage -> {
                mViewStates.setState {
                    copy(page = action.page)
                }
            }
            is LoginAction.Register -> {
                register(action.username, action.email, action.stuId,
                    action.code, action.pwd, action.name)
            }
            is LoginAction.SendRegisterCode -> sendRegisterCode(action.email)
            is LoginAction.Login -> login(action.username, action.pwd)
            is LoginAction.SendPwdCode -> sendChangePwdCode(action.email)
            is LoginAction.ChangePwd -> {
                viewModelScope.launch {
                    try {
                        val result = RetrofitHelper.userService.resetPwd(action.code, action.pwd)
                        if (result.ok) {
                            mViewEvents.triggerEvent(LoginEvent.ChangePwdSuccess)
                        }
                    } catch (e: Exception) {
                        mViewEvents.triggerEvent(LoginEvent.ChangePwdFailed)
                    }
                }
            }
        }
    }


    private fun register(username: String, email: String, stuId: String,
                         code: String, pwd: String, name: String) {
        viewModelScope.launch {
            mViewEvents.triggerEvent(UserManager.register(username, email, stuId, code, pwd, name))
        }
    }

    private fun sendRegisterCode(email: String) {
        viewModelScope.launch {
            mViewEvents.triggerEvent(UserManager.registerMail(email))
        }
    }

    private fun sendChangePwdCode(email: String) {
        viewModelScope.launch {
            mViewEvents.triggerEvent(UserManager.changePwdMail(email))
        }
    }

    private fun login(username: String, pwd: String) {
        viewModelScope.launch {
            mViewEvents.triggerEvent(UserManager.login(username, pwd))
        }
    }
}