package cn.skygard.happyoj.repo.remote.model

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.HttpException

data class Result<T>(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val message: String,
    @SerializedName("ok")
    val ok: Boolean,
    @SerializedName("data")
    val data: T
) {
    companion object {
        private val gson = Gson()
        fun onError(e: Throwable?) {
            Log.d("Result", e?.message.toString())
            if (e is HttpException) {
                Log.d("Result", e.response()?.errorBody()?.string().toString())
            }
        }

        fun parseError(e: HttpException): Result<Any>? {
            val string = e.response()?.errorBody()?.string()
            if (string != null) {
                return gson.fromJson<Result<Any>>(string, Result::class.java)
            }
            return null
        }
    }
}