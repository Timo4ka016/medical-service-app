package dts.app.med_app_android.Service

import dts.app.med_app_android.Model.AuthenticationRequest
import dts.app.med_app_android.Model.RegisterDoctorRequest
import dts.app.med_app_android.Model.ReturnedToken
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("api/auth/login")
    fun authenticate(@Body request: AuthenticationRequest): Call<ReturnedToken>

    @POST("api/auth/create-doctor")
    fun registerDoctor(@Body request: RegisterDoctorRequest): Call<ReturnedToken>

}