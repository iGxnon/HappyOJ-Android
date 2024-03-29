@file:Suppress("UNUSED")
package cn.skygard.common.mvi.vm

import androidx.lifecycle.*
import cn.skygard.common.mvi.events.MutableMultiLiveEvents

abstract class BaseViewModel<State, Action, Event>(state: State) : ViewModel() {

    protected val mViewStates = MutableLiveData(state)
    val viewStates = mViewStates as LiveData<State>
    val viewStatesFlow = mViewStates.asFlow()

    protected val mViewEvents = MutableMultiLiveEvents<Event>()
    val viewEvents = mViewEvents as LiveData<List<Event>>
    val viewEventsFlow = mViewEvents.asFlow()

    /**
     * 分发 action 切换 state
     */
    abstract fun dispatch(action: Action)

}