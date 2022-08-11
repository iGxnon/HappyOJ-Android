package cn.skygard.common.mvi.events

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 借用 LiveData 实现 Events
 * 参考 https://github.com/RicardoJiang/android-architecture/blob/main/mvi-core/src/main/java/com/zj/mvi/core/LiveEvents.kt
 */
open class MutableMultiLiveEvents<T> : MutableLiveData<List<T>>() {

    private val observers = hashSetOf<ObserverWrapper<in T>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in List<T>>) {
        observers.find { it.observer === observer }?.let { _ -> // existing
            return
        }
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observe(owner, wrapper)
    }

    @MainThread
    override fun observeForever(observer: Observer<in List<T>>) {
        observers.find { it.observer === observer }?.let { _ -> // existing
            return
        }
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observeForever(wrapper)
    }

    @MainThread
    override fun removeObserver(observer: Observer<in List<T>>) {
        if (observer is ObserverWrapper<*> && observers.remove(observer)) {
            super.removeObserver(observer)
            return
        }
        val iterator = observers.iterator()
        while (iterator.hasNext()) {
            val wrapper = iterator.next()
            if (wrapper.observer == observer) {
                iterator.remove()
                super.removeObserver(wrapper)
                break
            }
        }
    }

    @MainThread
    override fun setValue(t: List<T>?) {
        observers.forEach { it.newValue(t) }
        super.setValue(t)
    }

    private class ObserverWrapper<T>(val observer: Observer<in List<T>>) : Observer<List<T>> {

        private val pending = AtomicBoolean(false)
        // 可以先把事件 List 缓存起来，当数据发生变化(Event发生)后通知观察者
        private val eventList = mutableListOf<List<T>>()
        override fun onChanged(t: List<T>?) {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(eventList.flatten())
                eventList.clear()
            }
        }

        fun newValue(t: List<T>?) {
            pending.set(true)
            t?.let {
                eventList.add(it)
            }
        }
    }

}