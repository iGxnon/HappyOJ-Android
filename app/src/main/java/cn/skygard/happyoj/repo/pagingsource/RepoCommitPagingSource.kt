package cn.skygard.happyoj.repo.pagingsource

import cn.skygard.common.base.adapter.BasePagingSource
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import cn.skygard.happyoj.repo.remote.model.RepoCommit

class RepoCommitPagingSource(private val repoUrl: String) : BasePagingSource<RepoCommit>() {

    override suspend fun getData(page: Int): List<RepoCommit> {
        // repoUrl: http(s)://github.com/{owner}/repo(/)
        val ids = repoUrl
            .replace("https://", "")
            .replace("http://", "")
            .split("/")
            .filterNot {
                it.contains("github.com")
            }.filterNot {
                it.isEmpty()
            }
        assert(ids.size == 2)
        return RetrofitHelper.repoService.getRepoCommits(ids[0], ids[1], page + 1, 25)
    }

}