package cn.skygard.common.mvi

import androidx.lifecycle.ViewModelProvider
import cn.skygard.common.base.ext.getGenericClass
import cn.skygard.common.base.ui.BaseFragment
import cn.skygard.common.mvi.vm.BaseViewModel

abstract class BaseVmFragment<VM: BaseViewModel<*, *, *>> : BaseFragment() {

    protected val viewModel by lazy {
        val clazz = javaClass.getGenericClass<VM, BaseViewModel<*, *, *>>()
        if (viewModelFactory == null) {
            ViewModelProvider(this).get(clazz)
        } else {
            ViewModelProvider(this, viewModelFactory!!).get(clazz)
        }
    }

    protected open val viewModelFactory: ViewModelProvider.Factory? = null

}