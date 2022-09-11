package cn.skygard.happyoj.intent.vm

import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.intent.state.MainSharedEvent
import cn.skygard.happyoj.intent.state.SearchAction
import cn.skygard.happyoj.intent.state.SearchState

// 这是一个共享的 ViewModel，被 SearchActivity, SearchHistoryFragment, SearchFragment 共享
class SearchViewModel : BaseViewModel<SearchState, SearchAction, MainSharedEvent>(SearchState()) {
    override fun dispatch(action: SearchAction) {
    }
}