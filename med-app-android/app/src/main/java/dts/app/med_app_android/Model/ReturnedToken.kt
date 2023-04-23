package dts.app.med_app_android.Model

import com.google.gson.annotations.SerializedName

data class ReturnedToken(
    @SerializedName("token")
    val token: String,
    @SerializedName("user_type")
    val role: String

)