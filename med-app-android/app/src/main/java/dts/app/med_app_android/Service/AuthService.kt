package dts.app.med_app_android.Service

import dts.app.med_app_android.Model.AuthenticationRequest
import dts.app.med_app_android.Model.RegisterClientRequest
import dts.app.med_app_android.Model.RegisterDoctorRequest
import dts.app.med_app_android.Model.ReturnedToken
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("api/auth/login")
    fun authenticate(
        @Header("No-Auth") noAuth: Boolean = true,
        @Body request: AuthenticationRequest
    ): Call<ReturnedToken>

    @POST("api/auth/create-doctor")
    fun registerDoctor(
        @Body request: RegisterDoctorRequest,
        @Header("No-Auth") noAuth: Boolean = true
    ): Call<ReturnedToken>

    @POST("api/auth/create-client")
    fun registerClient(
        @Header("No-Auth") noAuth: Boolean = true,
        @Body request: RegisterClientRequest
    ): Call<ReturnedToken>

}