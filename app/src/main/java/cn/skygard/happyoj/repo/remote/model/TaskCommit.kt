package cn.skygard.happyoj.repo.remote.model
import com.google.gson.annotations.SerializedName


data class TaskCommit(
    @SerializedName("commit_index")
    val commitIndex: CommitIndex
) {
    data class CommitIndex(
        @SerializedName("attr")
        val attr: Int,
        @SerializedName("create_time")
        val createTime: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("id")
        val id: Long,
        @SerializedName("nickname")
        val nickname: String,
        @SerializedName("repo_url")
        val repoUrl: String,
        @SerializedName("state")
        val state: Int,
        @SerializedName("task_id")
        val taskId: Long,
        @SerializedName("update_time")
        val updateTime: String,
        @SerializedName("user_id")
        val userId: Long
    )
}

data class TaskCommits(
    @SerializedName("commit_indices")
    val commitIndices: List<TaskCommit.CommitIndex>
)