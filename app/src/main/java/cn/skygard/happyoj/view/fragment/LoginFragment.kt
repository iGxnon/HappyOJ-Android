package cn.skygard.happyoj.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import cn.skygard.common.base.ui.BaseBindFragment
import cn.skygard.common.mvi.ext.observeState
import cn.skygard.happyoj.databinding.FragmentLoginBinding
import cn.skygard.happyoj.intent.state.LoginAction
import cn.skygard.happyoj.intent.state.LoginState
import cn.skygard.happyoj.intent.state.Page
import cn.skygard.happyoj.intent.vm.LoginViewModel

class LoginFragment : BaseBindFragment<FragmentLoginBinding>() {

    private val viewModel by activityViewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            btnLogin.setOnClickListener(LoginCallback())
            btnRegister.setOnClickListener {
                viewModel.dispatch(LoginAction.ChangePage(page = Page.REGISTER))
            }
            tvForgetPwd.setOnClickListener {
                viewModel.dispatch(LoginAction.ChangePage(page = Page.CHANGE_PWD))
            }
        }
    }

    private inner class LoginCallback : View.OnClickListener {
        override fun onClick(v: View?) {
            binding.run {
                val username = etUsername.text.toString()
                val pwd = etPwd.text.toString()
                viewModel.dispatch(LoginAction.Login(username, pwd))
            }
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

}