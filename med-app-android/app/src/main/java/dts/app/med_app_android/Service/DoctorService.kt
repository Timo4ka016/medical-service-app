package dts.app.med_app_android.Service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.PUT

interface DoctorService {

    @PUT("api/doctor/city/add")
    @FormUrlEncoded
    fun addCityToDoctor(@Field("cityId") cityId: Long): Call<Void>

    @PUT("api/doctor/category/add")
    fun addCategoryToDoctor(@Body categoryIds: List<Long>): Call<Void>

}