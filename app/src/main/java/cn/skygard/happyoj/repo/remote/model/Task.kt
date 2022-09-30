package cn.skygard.happyoj.repo.remote.model
import com.google.gson.annotations.SerializedName
import java.util.*

data class Tasks(
    @SerializedName("task_subject")
    val taskSubject: List<Task.TaskSubject>
)

data class Task(
    @SerializedName("task_subject")
    val taskSubject: TaskSubject
) {
    data class TaskSubject(
        @SerializedName("attr")
        val attr: Int = 0,
        @SerializedName("create_time")
        var createTime: String? = null,
        @SerializedName("deadline")
        var deadline: String? = null,
        @SerializedName("id")
        val id: Long,
        @SerializedName("image_url")
        val imageUrl: String,
        @SerializedName("md_text")
        var mdText: String? = null,
        @SerializedName("state")
        val state: Int = 0,
        @SerializedName("summary")
        val summary: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("update_time")
        val updateTime: String,

        var submitState: Boolean? = null,
        var repoState: String? = "",
        var commentState: Boolean? = null,
    )
}

