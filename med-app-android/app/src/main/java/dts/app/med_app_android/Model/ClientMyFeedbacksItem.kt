package dts.app.med_app_android.Model

data class ClientMyFeedbacksItem(
    val client: Client,
    val id: Int,
    val rating: Double,
    val text: String,
    val doctorId: Long
)