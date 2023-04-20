package dts.app.med_app_android.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import dts.app.med_app_android.Model.RegisterDoctorRequest
import dts.app.med_app_android.Model.ReturnedToken
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Service.AuthService
import dts.app.med_app_android.TokenManager
import dts.app.med_app_android.databinding.RegistrationDoctorBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class RegistrationDoctor : Fragment() {
    private lateinit var binding: RegistrationDoctorBinding
    private lateinit var tokenManager: TokenManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RegistrationDoctorBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        spinners()
        register()
        return binding.root
    }

    private fun register() = with(binding) {
        buttonRegister.setOnClickListener {
            val authService = RetrofitClient.getRetrofitClient()?.create(AuthService::class.java)
            val request = RegisterDoctorRequest(
                email = inputEmail.text.toString(),
                password = inputPassword.text.toString(),
                firstname = inputFirstname.text.toString(),
                lastname = inputLastname.text.toString(),
                phoneNumber = inputPassword.text.toString()
            )

            val call = authService?.registerDoctor(request)

            call?.enqueue(object : Callback<ReturnedToken> {
                override fun onResponse(
                    call: Call<ReturnedToken>,
                    response: Response<ReturnedToken>
                ) {
                    if (response.isSuccessful) {
                        val token = response.body()?.token
                        tokenManager.saveToken(token.toString())

                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ReturnedToken>, t: Throwable) {
                    Toast.makeText(context, "Bruh", Toast.LENGTH_SHORT).show()
                }
            })

        }
    }

    private fun spinners() = with(binding) {
        val cities = resources.getStringArray(R.array.cities)
        val cityAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, cities)
        spinnerCity.adapter = cityAdapter

    }
}