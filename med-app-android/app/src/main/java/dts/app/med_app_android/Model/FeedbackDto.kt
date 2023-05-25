package dts.app.med_app_android.Model

data class FeedbackDto(
    val id: Int,
    val rating: Double,
    val client: FullNameClient,
    val text: String
)