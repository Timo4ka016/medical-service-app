package dts.app.med_app_android.Service

import dts.app.med_app_android.Model.UpdateClientRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.PUT

interface ClientService {

    @PUT("api/client/city/add")
    @FormUrlEncoded
    fun addCityToClient(@Field("cityId") cityId: Long): Call<Void>

    @PUT("api/client/profile/update")
    fun updateClient(@Body request: UpdateClientRequest): Call<Void>

}