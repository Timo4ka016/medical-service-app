package dts.app.med_app_android.Model

data class RegisterClientRequest(
    val email: String,
    val firstname: String,
    val lastname: String,
    val password: String
)