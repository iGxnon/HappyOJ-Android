package cn.skygard.happyoj.repo.pagingsource

import cn.skygard.common.base.adapter.BasePagingSource
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import cn.skygard.happyoj.repo.remote.model.Task

class TasksPagingSource : BasePagingSource<Task.TaskSubject>() {

    override suspend fun getData(page: Int): List<Task.TaskSubject> {
        return RetrofitHelper.taskService.getTasks(10, page + 1).data.taskSubject
    }

}