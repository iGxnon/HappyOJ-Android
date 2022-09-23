package cn.skygard.happyoj.intent.vm

import android.util.Log
import androidx.lifecycle.viewModelScope
import cn.skygard.common.base.ext.defaultSp
import cn.skygard.common.mvi.ext.setState
import cn.skygard.common.mvi.ext.triggerEvent
import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.domain.logic.UserManager
import cn.skygard.happyoj.intent.state.LoginAction
import cn.skygard.happyoj.intent.state.LoginEvent
import cn.skygard.happyoj.intent.state.LoginState
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception

class LoginViewModel : BaseViewModel<LoginState, LoginAction, LoginEvent>(LoginState()) {
    override fun dispatch(action: LoginAction) {
        Log.d("LoginViewModel", "received an action $action")
        when (action) {
            is LoginAction.ChangePage -> {
                mViewStates.setState {
                    copy(isLoginPage = action.isLoginPage)
                }
            }
            is LoginAction.Register -> {
                register(action.username, action.email, action.stuId,
                    action.code, action.pwd, action.name)
            }
            is LoginAction.SendCode -> sendCode(action.email)
            is LoginAction.Login -> login(action.username, action.pwd)
        }
    }


    private fun register(username: String, email: String, stuId: String,
                         code: String, pwd: String, name: String) {
        viewModelScope.launch {
            mViewEvents.triggerEvent(UserManager.register(username, email, stuId, code, pwd, name))
        }
    }

    private fun sendCode(email: String) {
        viewModelScope.launch {
            mViewEvents.triggerEvent(UserManager.registerMail(email))
        }
    }

    private fun login(username: String, pwd: String) {
        viewModelScope.launch {
            mViewEvents.triggerEvent(UserManager.login(username, pwd))
        }
    }
}