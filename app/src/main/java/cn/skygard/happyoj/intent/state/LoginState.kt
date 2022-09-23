package cn.skygard.happyoj.intent.state

data class LoginState(
    val isLoginPage: Boolean = true,
)

sealed class LoginAction {
    data class ChangePage(val isLoginPage: Boolean) : LoginAction()
    data class Register(val username: String, val email: String, val stuId: String,
                        val code: String, val pwd: String, val name: String) : LoginAction()
    data class SendCode(val email: String) : LoginAction()
    data class Login(val username: String, val pwd: String) : LoginAction()
}

sealed class LoginEvent {
    object LoginFailed : LoginEvent()
    object LoginSuccess : LoginEvent()
    object RegisterSuccess: LoginEvent()
    object RegisterFailed: LoginEvent()
    object MailSuccess: LoginEvent()
    object MailFailed: LoginEvent()
}