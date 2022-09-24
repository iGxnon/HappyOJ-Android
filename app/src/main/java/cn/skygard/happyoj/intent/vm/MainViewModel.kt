package cn.skygard.happyoj.intent.vm

import android.util.Log
import androidx.lifecycle.viewModelScope
import cn.skygard.common.base.ext.defaultSp
import cn.skygard.common.mvi.ext.triggerEvent
import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.domain.model.User
import cn.skygard.happyoj.intent.state.LoginEvent
import cn.skygard.happyoj.intent.state.MainAction
import cn.skygard.happyoj.intent.state.MainSharedEvent
import cn.skygard.happyoj.intent.state.MainState
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception

class MainViewModel :
    BaseViewModel<MainState, MainAction, MainSharedEvent>(MainState()) {

    override fun dispatch(action: MainAction) {
        Log.d("MainViewModel", "received an action $action")
        when (action) {
            is MainAction.TriggerSharedEvent -> {
                mViewEvents.triggerEvent(action.event)
            }
            is MainAction.RefreshData -> refreshData()
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            try {
                RetrofitHelper.userService.getInfo().run {
                    if (this.ok) {
                        defaultSp.edit()
                            .putString("name", this.data.userSubject.username)
                            .putString("avatar_url", this.data.userSubject.picture)
                            .putString("email", this.data.userSubject.email)
                            .apply()
                        Log.d("MainViewModel", "refresh data to ${this.data.userSubject}")
                        mViewEvents.triggerEvent(MainSharedEvent.RefreshProfile)
                    }
                }
            } catch (e: Exception) {
                if (e is HttpException) {
                    Log.d("MainViewModel", User.fromSp().toString())
                    Log.d("MainViewModel", e.response()?.errorBody()?.string()?:"")
                }
            }
        }
    }

}