package dts.app.med_app_android.Model

data class MyAppointmentsDtoItem(
    val ad: AdInfo,
    val appointmentTime: String,
    val client: FullNameUser,
    val desiredPrice: Long,
    val doctor: FullNameUser,
    val id: Long,
    val message: String,
    val status: String
)