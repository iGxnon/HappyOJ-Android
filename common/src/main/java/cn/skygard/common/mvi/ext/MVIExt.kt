package cn.skygard.common.mvi.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.MutableLiveData
import cn.skygard.common.mvi.events.MutableMultiLiveEvents
import java.lang.RuntimeException
import kotlin.reflect.KProperty1

fun <T, A> LiveData<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    action: (A) -> Unit
) {
    Transformations.map(this) {
        prop1.get(it)
    }.distinctUntilChanged().observe(lifecycleOwner) {
        action.invoke(it)
    }
}

fun <T, A, B> LiveData<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    prop2: KProperty1<T, B>,
    action: (A, B) -> Unit
) {
    Transformations.map(this) {
        StateTuple2(prop1.get(it), prop2.get(it))
    }.distinctUntilChanged().observe(lifecycleOwner) { (a, b) ->
        action.invoke(a, b)
    }
}

fun <T, A, B, C> LiveData<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    prop2: KProperty1<T, B>,
    prop3: KProperty1<T, C>,
    action: (A, B, C) -> Unit
) {
    Transformations.map(this) {
        StateTuple3(prop1.get(it), prop2.get(it), prop3.get(it))
    }.distinctUntilChanged().observe(lifecycleOwner) { (a, b, c) ->
        action.invoke(a, b, c)
    }
}

internal data class StateTuple2<A, B>(val a: A, val b: B)
internal data class StateTuple3<A, B, C>(val a: A, val b: B, val c: C)

fun <T> MutableLiveData<T>.setState(reducer: T.() -> T) {
    if (this.value == null) {
        throw RuntimeException("cannot setState to a uninitialized live data")
    }
    this.value = this.value!!.reducer()
}

/**
 * viewModel 触发单条事件
 */
fun <T> MutableMultiLiveEvents<T>.triggerEvent(value: T) {
    this.value = listOf(value)
}

/**
 * viewModel 触发多条事件
 */
fun <T> MutableMultiLiveEvents<T>.triggerEvents(vararg values: T) {
    this.value = values.toList()
}

/**
 * lifecycleOwner 监听事件
 */
fun <T> LiveData<List<T>>.observeEvent(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) {
    this.observe(lifecycleOwner) {
        it.forEach { event ->
            action.invoke(event)
        }
    }
}

inline fun <T, R> withState(state: LiveData<T>, block: (T) -> R): R? {
    return state.value?.let(block)
}