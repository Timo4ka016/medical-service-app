package dts.app.med_app_android.Service

import dts.app.med_app_android.Model.ClientMainInfo
import dts.app.med_app_android.Model.GetDoctorAdById
import dts.app.med_app_android.Model.GetDoctorAdsItem
import dts.app.med_app_android.Model.UpdateClientRequest
import retrofit2.Call
import retrofit2.http.*

interface ClientService {

    @GET("api/client/profile")
    fun profileClient(): Call<ClientMainInfo>

    @PUT("api/client/city/add")
    @FormUrlEncoded
    fun addCityToClient(@Field("cityId") cityId: Long): Call<Void>

    @PUT("api/client/profile/update")
    fun updateClient(@Body request: UpdateClientRequest): Call<Void>

    @GET("api/client/ads/ad")
    fun getAdById(@Query("adId") adId: Long): Call<GetDoctorAdById>

    @GET("api/client/recommendations")
    fun getRecommendations(): Call<List<GetDoctorAdsItem>>

    @GET("api/client/ads/ad/category")
    fun getAdByCategory(@Query("categoryId") categoryId: Long): Call<List<GetDoctorAdsItem>>

}