package cn.skygard.happyoj.view.activity

import android.os.Bundle
import cn.skygard.common.mvi.BaseVmBindActivity
import cn.skygard.happyoj.databinding.ActivityMainBinding
import cn.skygard.happyoj.intent.vm.MainViewModel

class MainActivity : BaseVmBindActivity<MainViewModel, ActivityMainBinding>() {

    override val isCancelStatusBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        initView()
        initViewStates()
        initViewEvents()
    }

    private fun initView() {

    }

    private fun initViewStates() {

    }

    private fun initViewEvents() {

    }
}