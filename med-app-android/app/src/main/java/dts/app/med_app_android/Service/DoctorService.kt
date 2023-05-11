package dts.app.med_app_android.Service

import dts.app.med_app_android.Model.CreateAdRequest
import dts.app.med_app_android.Model.GetMyAdsRequest
import dts.app.med_app_android.Model.GetMyAdsRequestItem
import dts.app.med_app_android.Model.UpdateDoctorRequest
import retrofit2.Call
import retrofit2.http.*

interface DoctorService {

    @PUT("api/doctor/city/add")
    @FormUrlEncoded
    fun addCityToDoctor(@Field("cityId") cityId: Long): Call<Void>

    @PUT("api/doctor/category/add")
    fun addCategoryToDoctor(@Body categoryIds: List<Long>): Call<Void>

    @PUT("api/doctor/profile/update")
    fun updateDoctor(@Body request: UpdateDoctorRequest): Call<Void>

    @POST("api/doctor/ads/ad/create")
    fun createAd(
        @Query("categoryId") categoryId: Long,
        @Body request: CreateAdRequest
    ): Call<Void>

    @GET("api/doctor/ads")
    fun getMyAds():Call<List<GetMyAdsRequestItem>>
}