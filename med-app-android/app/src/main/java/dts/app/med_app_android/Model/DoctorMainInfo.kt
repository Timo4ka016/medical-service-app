package dts.app.med_app_android.Model

data class DoctorMainInfo(
    val id: Long,
    val email: String,
    val firstname: String,
    val lastname: String,
    val phoneNumber: String,
    val description: String,
    val city: String,
    val rating: Double,
    val receivedFeedbacks: List<FeedbackDto>
)