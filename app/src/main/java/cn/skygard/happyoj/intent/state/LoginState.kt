package cn.skygard.happyoj.intent.state

data class LoginState(
    val isLoginPage: Boolean = true,
    val registerPwdError: String = ""
)

sealed class LoginAction {
    data class ChangePage(val isLoginPage: Boolean) : LoginAction()
    data class ShowRegisterPwdError(val err: String) : LoginAction()
}

sealed class LoginEvent {
    object LoginFaild : LoginEvent()
    object LoginSuccess: LoginEvent()
}