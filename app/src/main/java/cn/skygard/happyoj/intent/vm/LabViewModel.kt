package cn.skygard.happyoj.intent.vm

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cn.skygard.common.base.BaseApp
import cn.skygard.common.mvi.ext.setState
import cn.skygard.common.mvi.ext.triggerEvent
import cn.skygard.common.mvi.vm.BaseViewModel
import cn.skygard.happyoj.intent.state.FetchState
import cn.skygard.happyoj.intent.state.LabAction
import cn.skygard.happyoj.intent.state.LabEvent
import cn.skygard.happyoj.intent.state.LabState
import cn.skygard.happyoj.repo.database.AppDatabase
import cn.skygard.happyoj.repo.database.entity.TaskEntity
import cn.skygard.happyoj.repo.pagingsource.RepoCommitPagingSource
import cn.skygard.happyoj.repo.pagingsource.TasksPagingSource
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import cn.skygard.happyoj.repo.remote.model.RepoCommit
import cn.skygard.happyoj.repo.remote.model.Result
import cn.skygard.happyoj.repo.remote.model.Task
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LabViewModel(private val taskItem: Task.TaskSubject)
    : BaseViewModel<LabState, LabAction, LabEvent>(LabState()) {

    private var pagingData: Flow<PagingData<RepoCommit>>? = null

    private fun initPagingData() {
        // 初始化 RepoCommit 的 pagingData
        viewModelScope.launch {
            try {
                val commit = RetrofitHelper.taskService.getTaskCommit(taskItem.id)
                if (commit.ok) {
                    pagingData = Pager(
                        config = PagingConfig(
                            pageSize = 20,
                            enablePlaceholders = false,
                            initialLoadSize = 20,
                        ),
                        pagingSourceFactory = {
                            RepoCommitPagingSource(commit.data.commitIndex.repoUrl)
                        }
                    ).flow.catch { e ->
                        Result.onError(e.cause)
                    }.cachedIn(viewModelScope)
                }
            } catch (ignore: Exception) {}
            pagingData?.run {
                collectLatest { data ->
                    mViewStates.setState {
                        copy(repoCommitPaging = data)
                    }
                }
            }
        }
    }

    init {
        initPagingData()
    }


    override fun dispatch(action: LabAction) {
        Log.d("LabViewModel", "received an action $action")
        when (action) {
            is LabAction.HideMenu -> mViewStates.setState {
                copy(menuVisibility = false)
            }
            is LabAction.ShowMenu -> mViewStates.setState {
                copy(menuVisibility = true)
            }
            is LabAction.ChangePage -> mViewStates.setState {
                copy(currentPage = action.page)
            }
            is LabAction.ScrollToTop -> mViewEvents.triggerEvent(LabEvent.ScrollToTop)
            is LabAction.FetchContent -> fetchContent(action.noCache)
            is LabAction.SubmitRepoUrl -> submitRepoUrl(action.url, action.desc)
            is LabAction.RepoCommitRefresh -> {
                if (pagingData == null) {
                    initPagingData()
                }
                if (pagingData == null) {
                    mViewEvents.triggerEvent(LabEvent.TriggerRefreshRepoCommit(false))
                } else {
                    mViewEvents.triggerEvent(LabEvent.TriggerRefreshRepoCommit(true))
                }
            }
        }
    }

    private fun submitRepoUrl(url: String, desc: String = "") {
        viewModelScope.launch {
            try {
                RetrofitHelper.taskService.getTaskCommit(taskItem.id).let { commit ->
                    if (commit.ok) {
                        if (RetrofitHelper.taskService
                                .patchRepo(
                                    cid = commit.data.commitIndex.id,
                                    repoUrl = url,
                                    description = desc
                                ).ok) {
                            mViewEvents.triggerEvent(LabEvent.SubmitSuccess)
                        }
                    } else {
                        if (RetrofitHelper.taskService
                                .submitRepo(tid = taskItem.id,
                                    repoUrl = url, description = desc).ok) {
                            mViewEvents.triggerEvent(LabEvent.SubmitSuccess)
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is HttpException) {
                    Result.onError(e)
                }
                try {
                    if (RetrofitHelper.taskService
                            .submitRepo(tid = taskItem.id,
                                repoUrl = url, description = desc).ok) {
                        mViewEvents.triggerEvent(LabEvent.SubmitSuccess)
                    } else {
                        mViewEvents.triggerEvent(LabEvent.SubmitFailed)
                    }
                } catch (e: Exception) {
                    if (e is HttpException) {
                        Result.onError(e)
                    }
                    mViewEvents.triggerEvent(LabEvent.SubmitFailed)
                }
            }
        }
    }

    private var fetchContentJob: Job? = null
    private fun fetchContent(noCache: Boolean) {
        Log.d("LabViewModel", "fetching data from ${taskItem.id}, noCache: $noCache")
        mViewStates.setState {
            copy(contentFetchState = FetchState.Fetching)
        }
        fetchContentJob?.cancel()
        fetchContentJob = viewModelScope.launch {
            if (noCache) {
                if (!fetchContentOnline()) {
                    mViewStates.setState {
                        copy(contentFetchState = FetchState.NotFetched)
                    }
                }
            } else {
                if (!fetchContentOffline()) {
                    fetchContent(true)
                }
            }
        }
    }

    private suspend fun fetchContentOnline(): Boolean {
        try {
            RetrofitHelper.taskService.getTaskDetail(taskItem.id).let { result ->
                if (result.ok) {
                    mViewStates.setState {
                        copy(
                            contentFetchState = FetchState.Fetched,
                            mdContent = result.data.taskSubject.mdText!!
                        )
                    }
                }
                // 存入缓存，下次打开时会快一些
                result.data.taskSubject.let {
                    AppDatabase.INSTANCE.taskDao().insert(TaskEntity(
                        tid = it.id,
                        title = it.title,
                        summary = it.summary,
                        imageUrl = it.imageUrl,
                        mdContent = it.mdText!!,
                        date = it.updateTime,
                    ))
                }
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }

    private suspend fun fetchContentOffline(): Boolean {
        try {
            val content = AppDatabase.INSTANCE.taskDao().getContent(taskItem.id)?:return false
            mViewStates.setState {
                copy(
                    contentFetchState = FetchState.Fetched,
                    mdContent = content
                )
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

}