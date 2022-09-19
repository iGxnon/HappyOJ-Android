package cn.skygard.happyoj.repo.remote.model

import com.google.gson.annotations.SerializedName

data class Result<T>(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val message: String,
    @SerializedName("ok")
    val ok: Boolean,
    @SerializedName("data")
    val data: T
)