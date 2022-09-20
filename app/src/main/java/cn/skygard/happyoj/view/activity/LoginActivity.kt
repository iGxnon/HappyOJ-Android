package cn.skygard.happyoj.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import cn.skygard.common.base.ui.BaseBindActivity
import cn.skygard.happyoj.R
import cn.skygard.happyoj.databinding.ActivityLoginBinding
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginActivity : BaseBindActivity<ActivityLoginBinding>() {

    override val isCancelStatusBar: Boolean
        get() = false

    override val statusBarColor: Int
        get() = ContextCompat.getColor(this, R.color.prime_color)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
        binding.btnLogin.setOnClickListener(LoginCallback())
        binding.btnRegister.setOnClickListener(RegisterCallback())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, LoginActivity::class.java))
        }
    }

    private inner class LoginCallback : View.OnClickListener {
        override fun onClick(v: View?) {
        }
    }

    private inner class RegisterCallback : View.OnClickListener {
        val pattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
        override fun onClick(v: View?) {
            val email = binding.etUsername.text
            val pwd = binding.etPwd.text
            if (pwd.length > 30) {
                Toast.makeText(this@LoginActivity, "密码太长", Toast.LENGTH_SHORT).show()
                return
            }
            if (pwd.length < 6) {
                Toast.makeText(this@LoginActivity, "密码太短", Toast.LENGTH_SHORT).show()
                return
            }
            if (!Pattern.compile(pattern).matcher(email).matches()) {
                Toast.makeText(this@LoginActivity, "请输入邮箱注册", Toast.LENGTH_SHORT).show()
                return
            }
            lifecycleScope.launch {
                RetrofitHelper.userService.register("未命名", pwd.toString(), email.toString())
            }
        }
    }


}