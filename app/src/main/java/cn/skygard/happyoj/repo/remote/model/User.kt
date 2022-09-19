package cn.skygard.happyoj.repo.remote.model
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_subject")
    val subject: UserSubject
) {
    data class UserSubject(
        @SerializedName("id")
        val uid: Long,
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

    data class UserToken(
        @SerializedName("oauth2_token")
        val token: UserTokenWrapper
    ) {
        data class UserTokenWrapper(
            @SerializedName("access_token")
            val accessToken: Token,
            @SerializedName("refresh_token")
            val refreshToken: Token,
            @SerializedName("id_token")
            val idToken: Token
        )
        data class Token(
            @SerializedName("token_type")
            val type: String,
            @SerializedName("token_value")
            val value: String,
            @SerializedName("expires_at")
            val expiresAt: Long
        )
    }
}

