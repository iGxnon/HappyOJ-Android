package cn.skygard.happyoj.view.fragment

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import cn.skygard.common.base.ext.gone
import cn.skygard.common.base.ext.visible
import cn.skygard.common.base.ui.BaseBindFragment
import cn.skygard.happyoj.databinding.FragmentForgetPwdBinding
import cn.skygard.happyoj.intent.state.LoginAction
import cn.skygard.happyoj.intent.vm.LoginViewModel

class ChangePasswordFragment : BaseBindFragment<FragmentForgetPwdBinding>() {

    private val viewModel by activityViewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            tilEmail.visible()
            tilCode.gone()
            tilPwd.gone()
            tilPwdConfirm.gone()
            btnChange.gone()
            btnNext.visible()

            btnNext.setOnClickListener {
//                tilEmail.gone()
//                tilCode.visible()
//                tilPwd.visible()
//                tilPwdConfirm.visible()
//                btnChange.visible()
//                btnNext.gone()
                val email = etEmail.text.toString()
                if (!Utils.checkEmail(email)) {
                    "邮箱不符合规范".toast()
                    return@setOnClickListener
                }
                viewModel.dispatch(LoginAction.SendPwdCode(etEmail.text.toString()))
            }
//            etPwd.addTextChangedListener {
//                val pwd = etPwd.text.toString()
//                tilPwd.error = Utils.getPwdError(pwd)
//            }
//            etPwdConfirm.addTextChangedListener {
//                val pwd = etPwd.text.toString()
//                var err = Utils.getPwdError(pwd)
//                if (pwd != etPwd.text.toString()) {
//                    err += ",密码不一致"
//                }
//                tilPwd.error = err
//            }
//            btnChange.setOnClickListener {
//                val code = etCode.text.toString()
//                val pwd = etPwd.text.toString()
//                val pwdConfirm = etPwdConfirm.text.toString()
//                if (pwd != pwdConfirm) {
//                    "两次密码不一致".toast()
//                    return@setOnClickListener
//                }
//                viewModel.dispatch(LoginAction.ChangePwd(code, pwd))
//            }
        }
    }

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

}