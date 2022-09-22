package cn.skygard.happyoj.view.fragment

import android.os.Bundle
import android.view.View
import cn.skygard.common.base.ui.BaseBindFragment
import cn.skygard.happyoj.databinding.FragmentLoginBinding

class LoginFragment : BaseBindFragment<FragmentLoginBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            btnLogin.setOnClickListener(LoginCallback())
        }
    }

    private inner class LoginCallback : View.OnClickListener {
        override fun onClick(v: View?) {
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

}