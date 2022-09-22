package cn.skygard.happyoj.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import cn.skygard.common.base.ui.BaseBindFragment
import cn.skygard.happyoj.databinding.FragmentRegisterBinding
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegisterFragment : BaseBindFragment<FragmentRegisterBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            btnRegister.setOnClickListener(RegisterCallback())
        }
    }

    private inner class RegisterCallback : View.OnClickListener {
        val pattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
        override fun onClick(v: View?) {
            val email = binding.etUsername.text
            val pwd = binding.etPwd.text
            if (pwd.length > 30) {
                Toast.makeText(requireContext(), "密码太长", Toast.LENGTH_SHORT).show()
                return
            }
            if (pwd.length < 6) {
                Toast.makeText(requireContext(), "密码太短", Toast.LENGTH_SHORT).show()
                return
            }
            if (!Pattern.compile(pattern).matcher(email).matches()) {
                Toast.makeText(requireContext(), "请输入邮箱注册", Toast.LENGTH_SHORT).show()
                return
            }
            viewLifecycleScope.launch {
                RetrofitHelper.userService.register("未命名", pwd.toString(), email.toString())
            }
        }
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }

}