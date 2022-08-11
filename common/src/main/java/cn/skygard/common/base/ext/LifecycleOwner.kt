package cn.skygard.common.base.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email guo985892345@foxmail.com
 * @date 2022/7/25 20:58
 */
fun LifecycleOwner.launch(block: suspend CoroutineScope.() -> Unit): Job {
    return lifecycleScope.launch(block = block)
}

/**
 * 用于代替 lifecycleScope.launch 的更好的方法
 *
 * 有以下作用：
 * - 返回 [CatchSaver]，如果你想捕获异常，可以直接调用 .catch()，并且在你不捕获异常时会将异常往上抛出
 *
 * ## 背景：
 * 如果你想抓取 launch 的异常，正常写法是：
 * ```
 * lifecycleScope.launch(
 *   CoroutineExceptionHandler { context, exception ->
 *     exception.printStackTrace()
 *   }
 * ) {
 *   // ......
 * }
 * ```
 * 很明显就能看出这样写很不优雅，如果能像 rxjava 一样优雅就好了
 *
 * 那该怎么实现呢？
 *
 * 思路很简单，就是让函数返回一个对象，我们可以在返回的对象中设置异常的捕获，设置后才启动协程
 *
 * 使用方法：
 * ```
 * launchCatch {
 *   // 网络请求
 * }.catch {
 *   // 处理错误
 * }
 * ```
 * 上面这样写可以直接捕获网络请求中的异常
 *
 * # 注意：只有你主动写了 .catch {} 后它才启动协程
 * 如果想直接调用，建议使用前面的 LifecycleOwner.launch
 *
 * ```
 */
fun LifecycleOwner.launchCatch(block: suspend CoroutineScope.() -> Unit): CatchSaver {
    return CatchSaver(this.lifecycleScope, block)
}

class CatchSaver(
    private val lifecycleScope: CoroutineScope,
    private val block: suspend CoroutineScope.() -> Unit
) {

    /**
     * 直接运行，不抓取任何错误
     */
    fun runWithoutCatch(): Job {
        return lifecycleScope.launch(block = block)
    }

    /**
     * 抓取错误
     */
    fun catch(catch: CoroutineContext.(Throwable) -> Unit): Job {
        return lifecycleScope.launch(
            CoroutineExceptionHandler(catch),
            block = block
        )
    }
}