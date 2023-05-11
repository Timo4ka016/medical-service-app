package dts.app.med_app_android.Model

data class UpdateDoctorRequest(
    val description: String,
    val firstname: String,
    val lastname: String,
    val password: String,
    val phoneNumber: String,
    val cityId: Long?
)