package cn.skygard.common.mvi

import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import cn.skygard.common.base.ext.getGenericClass
import cn.skygard.common.base.ui.BaseBindFragment
import cn.skygard.common.mvi.vm.BaseViewModel

abstract class BaseVmBindFragment<VM: BaseViewModel<*, *, *>, VB: ViewBinding>
    : BaseBindFragment<VB>() {

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