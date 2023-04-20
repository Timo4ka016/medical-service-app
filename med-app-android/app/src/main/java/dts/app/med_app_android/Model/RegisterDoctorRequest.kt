package dts.app.med_app_android.Model

data class RegisterDoctorRequest(
    val email: String,
    val password: String,
    val firstname: String,
    val lastname: String,
    val phoneNumber: String
)