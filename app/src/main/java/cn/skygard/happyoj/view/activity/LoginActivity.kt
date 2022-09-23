package cn.skygard.happyoj.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import cn.skygard.common.base.BaseApp
import cn.skygard.common.base.ui.BaseBindActivity
import cn.skygard.common.mvi.BaseVmBindActivity
import cn.skygard.common.mvi.ext.observeEvent
import cn.skygard.common.mvi.ext.observeState
import cn.skygard.happyoj.R
import cn.skygard.happyoj.databinding.ActivityLoginBinding
import cn.skygard.happyoj.intent.state.LoginEvent
import cn.skygard.happyoj.intent.state.LoginState
import cn.skygard.happyoj.intent.vm.LoginViewModel
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import cn.skygard.happyoj.view.fragment.LoginFragment
import cn.skygard.happyoj.view.fragment.RegisterFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginActivity : BaseVmBindActivity<LoginViewModel, ActivityLoginBinding>() {

    override val isCancelStatusBar: Boolean
        get() = false

    override val statusBarColor: Int
        get() = ContextCompat.getColor(this, R.color.prime_color)

    override fun onCreate(savedInstanceState: Bundle?) {
        if (BaseApp.darkMode) {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        } else {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        }
        super.onCreate(savedInstanceState)
        initView()
        initViewStates()
        initViewEvents()
    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
        replaceFragment(R.id.frag_container) {
            LoginFragment.newInstance()
        }
    }

    private fun initViewEvents() {
        viewModel.viewEvents.run {
            observeEvent(this@LoginActivity) {
                val toastStr = when (it) {
                    is LoginEvent.LoginFailed -> "登录失败"
                    is LoginEvent.LoginSuccess -> {
                        finishAfterTransition()
                        "登录成功"
                    }
                    is LoginEvent.RegisterFailed -> "注册失败"
                    is LoginEvent.RegisterSuccess -> "注册成功"
                    is LoginEvent.MailSuccess -> "发送成功"
                    is LoginEvent.MailFailed -> "发送失败"
                }
                toastStr.toast()
            }
        }
    }

    private fun initViewStates() {
        viewModel.viewStates.run {
            observeState(this@LoginActivity, LoginState::isLoginPage) {
                if (it) {
                    binding.tvLogin.text = "登录"
                    replaceFragment(R.id.frag_container) {
                        LoginFragment.newInstance()
                    }
                } else {
                    binding.tvLogin.text = "注册"
                    replaceFragment(R.id.frag_container) {
                        RegisterFragment.newInstance()
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finishAfterTransition()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, LoginActivity::class.java))
        }
    }

}