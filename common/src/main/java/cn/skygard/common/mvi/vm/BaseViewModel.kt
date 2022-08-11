@file:Suppress("UNUSED")
package cn.skygard.common.mvi.vm

import androidx.lifecycle.*
import cn.skygard.common.mvi.events.MutableMultiLiveEvents

abstract class BaseViewModel<State, Action, Event> : ViewModel() {

    protected val mViewStates = MutableLiveData<State>()
    val viewStates = mViewStates as LiveData<State>

    protected val mViewEvents = MutableMultiLiveEvents<Event>()
    val viewEvents = mViewEvents as LiveData<List<Event>>

    /**
     * 分发 action 切换 state
     */
    abstract fun dispatch(action: Action)

}