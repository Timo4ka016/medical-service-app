package dts.app.med_app_android.Model

data class GetDoctorAdsItem(
    val address: String,
    val category: String,
    val doctor: DoctorMainInfo,
    val id: Int,
    val price: Int,
    val rating: Double,
    val title: String
)