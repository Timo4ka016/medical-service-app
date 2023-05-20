package dts.app.med_app_android.Service

import dts.app.med_app_android.Model.*
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
    fun getMyAds(): Call<List<GetDoctorAdsItem>>

    @GET("api/doctor/ads/ad")
    fun getDoctorAdById(@Query("adId") adId: Long): Call<GetDoctorAdById>

    @DELETE("api/doctor/ads/ad/delete")
    fun deleteAd(@Query("adId") adId: Long): Call<Void>

    @PUT("api/doctor/ads/ad/update")
    fun updateAd(
        @Query("adId") adId: Long,
        @Query("categoryId") categoryId: Long,
        @Body request: UpdateAdRequest
    ): Call<Void>

    @GET("api/doctor/profile")
    fun profileDoctor(): Call<DoctorMainInfo>
}