package cn.skygard.common.mvi

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import cn.skygard.common.base.ext.getGenericClass
import cn.skygard.common.base.ui.BaseBindFragment
import cn.skygard.common.mvi.vm.BaseViewModel

abstract class BaseVmBindFragment<VM: BaseViewModel<*, *, *>, VB: ViewBinding>
    : BaseBindFragment<VB>() {

    protected open val viewModelOwner: ViewModelStoreOwner
        get() = this

    protected val viewModel by lazy {
        val clazz = javaClass.getGenericClass<VM, BaseViewModel<*, *, *>>()
        if (viewModelFactory == null) {
            ViewModelProvider(viewModelOwner).get(clazz)
        } else {
            ViewModelProvider(viewModelOwner, viewModelFactory!!).get(clazz)
        }
    }

    protected open val viewModelFactory: ViewModelProvider.Factory? = null
}