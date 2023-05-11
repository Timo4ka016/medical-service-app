package dts.app.med_app_android.Model

data class GetMyAdsRequestItem(
    val address: String,
    val category: String,
    val description: String,
    val id: Int,
    val price: Int,
    val rating: Double,
    val title: String
)