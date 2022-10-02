package cn.skygard.happyoj.repo.pagingsource

import cn.skygard.common.base.adapter.BasePagingSource
import cn.skygard.happyoj.repo.database.AppDatabase
import cn.skygard.happyoj.repo.database.entity.TaskEntity
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import cn.skygard.happyoj.repo.remote.model.Task

class TasksPagingSource : BasePagingSource<Task.TaskSubject>() {

    override suspend fun getData(page: Int): List<Task.TaskSubject> {
        val tasks = RetrofitHelper.taskService.getTasks(10, page + 1).data.taskSubject
        for (task in tasks) {
            // 忽略获取提交记录或者评测记录的异常
            try {
                val commit = RetrofitHelper.taskService.getTaskCommit(task.id)
                if (commit.ok) {
                    task.submitState = true
                    val ids = Utils.parseRepoUrl(commit.data.commitIndex.repoUrl)
                    assert(ids.size == 2)
                    task.repoState = ids[1]
                    val comment =
                        RetrofitHelper.taskService.getTaskComment(commit.data.commitIndex.id)
                    if (comment.ok && comment.data.commitComment != null) {
                        task.commentState = true
                    }
                }
            }catch (ignore: Exception) {}
            val detail = RetrofitHelper.taskService.getTaskDetail(task.id)
            if (detail.ok) {
                task.deadline = detail.data.taskSubject.deadline
                task.mdText = detail.data.taskSubject.mdText
                task.createTime = detail.data.taskSubject.createTime
            }
            if (AppDatabase.INSTANCE.taskDao().getContent(task.id) == null) {
                AppDatabase.INSTANCE.taskDao().insert(TaskEntity(
                    tid = task.id,
                    title = task.title,
                    summary = task.summary,
                    imageUrl = task.imageUrl,
                    mdContent = task.mdText!!,
                    date = task.updateTime,
                    deadline = task.deadline!!
                ))
            }
        }
        return tasks
    }

}