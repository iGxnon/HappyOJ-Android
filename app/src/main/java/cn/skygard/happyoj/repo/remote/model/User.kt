package cn.skygard.happyoj.repo.remote.model
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_subject")
    val userSubject: UserSubject
) {
    data class UserSubject(
        @SerializedName("birthdate")
        val birthdate: String,
        @SerializedName("creat_time")
        val creatTime: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("email_verified")
        val emailVerified: Boolean,
        @SerializedName("family_name")
        val familyName: String,
        @SerializedName("gender")
        val gender: Int,
        @SerializedName("github_name")
        val githubName: String,
        @SerializedName("given_name")
        val givenName: String,
        @SerializedName("id")
        val id: Long,
        @SerializedName("last_ip")
        val lastIp: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("nickname")
        val nickname: String,
        @SerializedName("password")
        val password: String,
        @SerializedName("phone_number")
        val phoneNumber: String,
        @SerializedName("phone_number_verified")
        val phoneNumberVerified: Boolean,
        @SerializedName("picture")
        val picture: String,
        @SerializedName("scope")
        val scope: String,
        @SerializedName("state")
        val state: Int,
        @SerializedName("stu_id")
        val stuId: Int,
        @SerializedName("update_time")
        val updateTime: String,
        @SerializedName("username")
        val username: String
    )
}


data class Login(
    @SerializedName("oauth2_token")
    val oauth2Token: Oauth2Token
) {
    data class Oauth2Token(
        @SerializedName("access_token")
        val accessToken: AccessToken,
        @SerializedName("id_token")
        val idToken: IdToken,
        @SerializedName("refresh_token")
        val refreshToken: RefreshToken
    ) {

        data class AccessToken(
            @SerializedName("expires_at")
            val expiresAt: Int,
            @SerializedName("token_type")
            val tokenType: String,
            @SerializedName("token_value")
            val tokenValue: String
        )

        data class IdToken(
            @SerializedName("expires_at")
            val expiresAt: Int,
            @SerializedName("token_type")
            val tokenType: String,
            @SerializedName("token_value")
            val tokenValue: String
        )

        data class RefreshToken(
            @SerializedName("expires_at")
            val expiresAt: Int,
            @SerializedName("token_type")
            val tokenType: String,
            @SerializedName("token_value")
            val tokenValue: String
        )
    }
}



