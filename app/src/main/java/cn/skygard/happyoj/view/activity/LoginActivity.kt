package cn.skygard.happyoj.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import cn.skygard.common.base.BaseApp
import cn.skygard.common.base.ui.BaseBindActivity
import cn.skygard.happyoj.R
import cn.skygard.happyoj.databinding.ActivityLoginBinding
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import cn.skygard.happyoj.view.fragment.LoginFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginActivity : BaseBindActivity<ActivityLoginBinding>() {

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
//        binding.btnLogin.setOnClickListener(LoginCallback())
//        binding.btnRegister.setOnClickListener(RegisterCallback())
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

}