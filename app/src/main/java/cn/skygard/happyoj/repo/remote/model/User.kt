package cn.skygard.happyoj.repo.remote.model
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("aud")
    val aud: String,
    @SerializedName("exp")
    val exp: Int,
    @SerializedName("iat")
    val iat: Int,
    @SerializedName("iss")
    val iss: String,
    @SerializedName("nbf")
    val nbf: Int,
    @SerializedName("sub")
    val sub: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("user_details")
    val userDetails: UserDetails
) {
    data class UserDetails(
        @SerializedName("birthdate")
        val birthdate: String,
        @SerializedName("create_time")
        val createTime: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("email_verified")
        val emailVerified: Boolean,
        @SerializedName("family_name")
        val familyName: String,
        @SerializedName("gender")
        val gender: Int,
        @SerializedName("given_name")
        val givenName: String,
        @SerializedName("id")
        val id: Long,
        @SerializedName("name")
        val name: String,
        @SerializedName("nickname")
        val nickname: String,
        @SerializedName("phone_number")
        val phoneNumber: String,
        @SerializedName("phone_number_verified")
        val phoneNumberVerified: Boolean,
        @SerializedName("picture")
        val picture: String,
        @SerializedName("scope")
        val scope: String,
        @SerializedName("update_time")
        val updateTime: String,
        @SerializedName("username")
        val username: String
    )
}

