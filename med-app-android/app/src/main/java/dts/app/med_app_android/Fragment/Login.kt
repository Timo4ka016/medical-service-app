package dts.app.med_app_android.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import dts.app.med_app_android.Model.AuthenticationRequest
import dts.app.med_app_android.Model.ReturnedToken
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Service.AuthService
import dts.app.med_app_android.TokenManager
import dts.app.med_app_android.databinding.LoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Login : Fragment() {
    private lateinit var binding: LoginBinding
    private lateinit var tokenManager: TokenManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
//        login()

        return binding.root
    }


//    private fun login() = with(binding) {
//        buttonLogin.setOnClickListener {
//            val authService = RetrofitClient.getRetrofitClient(tokenManager)?.create(AuthService::class.java)
//
//            val request = AuthenticationRequest(
//                inputEmail.text.toString(),
//                inputPassword.text.toString()
//            )
//
//            val call = authService?.authenticate(request)
//
//            call?.enqueue(object : Callback<ReturnedToken> {
//                override fun onResponse(
//                    call: Call<ReturnedToken>,
//                    response: Response<ReturnedToken>
//                ) {
//                    if (response.isSuccessful) {
//                        val token = response.body()?.token
//                        tokenManager.saveToken(token.toString())
//
//                    } else {
//                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<ReturnedToken>, t: Throwable) {
//                    Toast.makeText(context, "Bruh", Toast.LENGTH_SHORT).show()
//                }
//            })
//
//        }
//    }

}