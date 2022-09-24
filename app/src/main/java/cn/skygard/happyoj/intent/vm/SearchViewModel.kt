package cn.skygard.happyoj.intent.vm

import android.util.Log
import cn.skygard.common.base.ext.defaultSp
import cn.skygard.common.base.ext.lazyUnlock
import cn.skygard.common.mvi.ext.setState
import cn.skygard.common.mvi.ext.triggerEvents
import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.intent.state.SearchAction
import cn.skygard.happyoj.intent.state.SearchEvent
import cn.skygard.happyoj.intent.state.SearchState
import kotlinx.coroutines.Job
import kotlin.collections.ArrayList

// 这是一个共享的 ViewModel，被 SearchActivity, SearchHistoryFragment, SearchFragment 共享
class SearchViewModel : BaseViewModel<SearchState, SearchAction, SearchEvent>(SearchState()) {

    private val quickSearchJob: Job? = null

    private val histories by lazyUnlock {
        ArrayList(defaultSp.getStringSet("history", emptySet())?.toList()?: emptyList())
    }

    private fun ArrayList<String>.addLimit(text: String) {
        while (this.size >= 15) {
            this.removeAt(0)
        }
        this.add(text)
    }

    override fun dispatch(action: SearchAction) {
        Log.d("SearchViewModel", "received an action $action")
        when (action) {
            is SearchAction.SearchFor -> searchFor(action.text)
            is SearchAction.UseHistory ->
                mViewEvents.triggerEvents(SearchEvent.ReplaceSearch, SearchEvent.StartSearch) // 会按顺序触发
            is SearchAction.SetSearchText -> mViewStates.setState {
                    copy(searchText = action.text)
            }
            is SearchAction.QuickSearchFor -> quickSearchFor(action.text)
            is SearchAction.GetHistory -> getHistory()
            is SearchAction.DeleteHistory -> deleteHistory(action.text)
        }
    }

    private fun getHistory() {
        mViewStates.setState {
            copy(history = ArrayList(histories))
        }
    }

    private fun searchFor(text: String) {
        histories.addLimit(text)
        defaultSp.edit()
            .putStringSet("history", histories.toSet())
            .apply()
    }

    private fun quickSearchFor(text: String) {
        quickSearchJob?.cancel()
    }

    private fun deleteHistory(text: String) {
        histories.remove(text)
        defaultSp.edit()
            .putStringSet("history", histories.toSet())
            .apply()
    }

}