package cn.skygard.common.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewbinding.ViewBinding
import cn.skygard.common.base.ext.getGenericClass
import cn.skygard.common.base.ext.lazyUnlock

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/6/2
 */
abstract class BaseBindActivity<VB: ViewBinding> : BaseActivity() {

    /**
     * 用于在调用 [setContentView] 之前的方法, 可用于设置一些主题或窗口的东西, 放这里不会报错
     */
    open fun onSetContentViewBefore() {}


    @Suppress("UNCHECKED_CAST")
    protected val binding: VB by lazyUnlock {
        val method = this.javaClass.getGenericClass<VB, ViewBinding>().getMethod(
            "inflate",
            LayoutInflater::class.java
        )
        val binding = method.invoke(null, layoutInflater) as VB
        binding
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onSetContentViewBefore()
        super.setContentView(binding.root)
        // 注意：这里已经 setContentView()，请不要自己再次调用，否则 ViewBinding 会失效
    }

    @Deprecated(
        "打个标记，因为使用了 ViewBinding，防止你忘记删除这个",
        level = DeprecationLevel.ERROR, replaceWith = ReplaceWith("")
    )
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }

    @Deprecated(
        "打个标记，因为使用了 ViewBinding，防止你忘记删除这个",
        level = DeprecationLevel.ERROR, replaceWith = ReplaceWith("")
    )
    override fun setContentView(view: View) {
        super.setContentView(view)
    }

}