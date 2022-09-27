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
        val createTime: String? = null,
        @SerializedName("deadline")
        val deadline: String? = null,
        @SerializedName("id")
        val id: Long,
        @SerializedName("image_url")
        val imageUrl: String,
        @SerializedName("md_text")
        val mdText: String? = null,
        @SerializedName("state")
        val state: Int = 0,
        @SerializedName("summary")
        val summary: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("update_time")
        val updateTime: String
    )
}

