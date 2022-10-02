package cn.skygard.happyoj.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import cn.skygard.common.base.ext.gone
import cn.skygard.common.base.ext.visible
import cn.skygard.common.base.ui.BaseBindFragment
import cn.skygard.happyoj.databinding.FragmentRegisterBinding
import cn.skygard.happyoj.intent.state.LoginAction
import cn.skygard.happyoj.intent.vm.LoginViewModel
import java.util.regex.Pattern

class RegisterFragment : BaseBindFragment<FragmentRegisterBinding>() {

    private val viewModel by activityViewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            switch(true)
            btnSendCode.setOnClickListener {
                switch(false)
                if (!checkEmail(etEmail.text.toString())) {
                    "邮箱不符合规范".toast()
                    return@setOnClickListener
                }
                viewModel.dispatch(LoginAction.SendCode(email = etEmail.text.toString()))
            }
            btnRegister.setOnClickListener(RegisterCallback())
            tvResend.setOnClickListener {
                if (!checkEmail(etEmail.text.toString())) {
                    "邮箱不符合规范".toast()
                    return@setOnClickListener
                }
                viewModel.dispatch(LoginAction.SendCode(email = etEmail.text.toString()))
            }
            etUsername.addTextChangedListener {
                val str = etUsername.text.toString()
                if (str.contains(" ")) {
                    tilUsername.error = "不能有空格"
                } else {
                    tilUsername.isErrorEnabled = false
                }
            }
            etPwd.addTextChangedListener {
                val pwd = etPwd.text.toString()
                val err = mutableListOf<String>()
                val errBuf = StringBuilder()
                if (pwd.length < 6 || pwd.length > 32)
                    err.add("长度6-32")
                if (pwd.lowercase() == pwd || pwd.uppercase() == pwd)
                    err.add("大小写字符")
                if (!Pattern.compile("[@!$^&*+_.:=|{}';,<>/?~]").matcher(pwd).find())
                    err.add("特殊字符")
                if (!Pattern.compile("[0-9]").matcher(pwd).find())
                    err.add("数字")
                err.joinTo(errBuf, separator = ",")
                tilPwd.error = errBuf
            }
        }
    }

    private fun switch(isSendCode: Boolean = true) {
        binding.run {
            if (isSendCode) {
                tilUsername.gone()
                tilCode.gone()
                tilPwd.gone()
                tilStuId.gone()
                tilName.gone()
                btnRegister.gone()
                tvResend.gone()
                btnSendCode.visible()
            } else {
                tilUsername.visible()
                tilCode.visible()
                tilPwd.visible()
                tilStuId.visible()
                tilName.visible()
                btnRegister.visible()
                tvResend.visible()
                btnSendCode.gone()
            }
        }
    }

    private inner class RegisterCallback : View.OnClickListener {
        override fun onClick(v: View?) {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val pwd = binding.etPwd.text.toString()
            val name = binding.etName.text.toString()
            val stuId = binding.etStuId.text.toString()
            val code = binding.etCode.text.toString()
            if (username.contains(" ") || username == "") {
                "用户名不符合规范".toast()
                return
            }
            if (!Regex("[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*").matches(name)) {
                "姓名不符合规范".toast()
                return
            }
            if (!Regex("[0-9]{10,11}").matches(stuId)) {
                "学号不符合规范".toast()
                return
            }
            if (!checkPwd(pwd)) {
                "密码不符合规范".toast()
                return
            }
            if (!checkEmail(email)) {
                "邮箱不符合规范".toast()
                return
            }
            viewModel.dispatch(LoginAction.Register(username, email, stuId, code, pwd, name))
        }
    }

    private fun checkPwd(pwd: String): Boolean {
        val pattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9])(.{6,32})\$"
        return Pattern.compile(pattern).matcher(pwd).matches()
    }

    private fun checkEmail(email: String): Boolean {
        val pattern = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
        return Pattern.compile(pattern).matcher(email).matches()
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }

}