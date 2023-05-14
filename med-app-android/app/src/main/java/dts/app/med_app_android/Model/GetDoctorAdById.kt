package dts.app.med_app_android.Model

data class GetDoctorAdById(
    val address: String,
    val category: String,
    val description: String,
    val city: String,
    val doctor: DoctorMainInfo,
    val id: Long,
    val price: Long,
    val rating: Double,
    val title: String
)