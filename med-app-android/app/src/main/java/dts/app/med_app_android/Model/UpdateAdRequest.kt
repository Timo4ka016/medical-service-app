package dts.app.med_app_android.Model

data class UpdateAdRequest(
    val address: String,
    val description: String,
    val price: Long,
    val title: String
)