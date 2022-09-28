package cn.skygard.happyoj.repo.remote.model
import com.google.gson.annotations.SerializedName


data class Comments(
    @SerializedName("commit_comment")
    val commitComment: List<CommitComment>?
) {
    data class CommitComment(
        @SerializedName("comment")
        val comment: String,
        @SerializedName("commit_id")
        val commitId: Long,
        @SerializedName("create_time")
        val createTime: String,
        @SerializedName("id")
        val id: Long,
        @SerializedName("score")
        val score: Int,
        @SerializedName("tutor_name")
        val tutorName: String,
        @SerializedName("update_time")
        val updateTime: String
    )
}

