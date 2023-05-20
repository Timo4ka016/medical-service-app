package dts.app.med_app_android.Retrofit


import android.util.Log
import dts.app.med_app_android.Constants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null
    fun getRetrofitClient(tokenManager: TokenManager): Retrofit {
        if (retrofit == null) {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor { chain ->
                val originalRequest = chain.request()
                val builder = originalRequest.newBuilder()

                if (originalRequest.header("No-Auth") == null) {
                    val token = tokenManager.getToken().toString()
                    if (token != null) {
                        builder.addHeader("Authorization", "Bearer $token")
                        Log.d("RetrofitClient", "Token: $token")
                    }
                }
                val newRequest = builder.build()
                chain.proceed(newRequest)
            }
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
        }
        return retrofit!!
    }
}