package dts.app.med_app_android.Model

data class Ad(
    val address: String,
    val category: Category,
    val doctor: FullNameUser,
    val id: Long,
    val price: Long,
    val rating: Double,
    val title: String
)