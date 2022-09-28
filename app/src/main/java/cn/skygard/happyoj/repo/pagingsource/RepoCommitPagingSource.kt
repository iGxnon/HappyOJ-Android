package cn.skygard.happyoj.repo.pagingsource

import cn.skygard.common.base.adapter.BasePagingSource
import cn.skygard.happyoj.repo.remote.RetrofitHelper
import cn.skygard.happyoj.repo.remote.model.RepoCommit

class RepoCommitPagingSource(private val repoUrl: String) : BasePagingSource<RepoCommit>() {

    override suspend fun getData(page: Int): List<RepoCommit> {
        // repoUrl: http(s)://github.com/{owner}/repo(/)
        val ids = Utils.parseRepoUrl(repoUrl)
        assert(ids.size == 2)
        return RetrofitHelper.repoService.getRepoCommits(ids[0], ids[1], page + 1, 25)
    }

}