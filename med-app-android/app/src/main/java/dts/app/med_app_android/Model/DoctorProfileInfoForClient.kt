package dts.app.med_app_android.Model

data class  DoctorProfileInfoForClient(
    val description: String,
    val firstname: String,
    val city: String,
    val id: Int,
    val lastname: String,
    val email: String,
    val phoneNumber: Long,
    val receivedFeedbacks: List<FeedbackDto>
)