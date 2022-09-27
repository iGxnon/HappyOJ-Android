package cn.skygard.happyoj.repo.remote.model
import com.google.gson.annotations.SerializedName


data class RepoBranch(
    @SerializedName("commit")
    val commit: Commit,
    @SerializedName("name")
    val name: String,
    @SerializedName("protected")
    val `protected`: Boolean
) {
    data class Commit(
        @SerializedName("sha")
        val sha: String,
        @SerializedName("url")
        val url: String
    )
}
