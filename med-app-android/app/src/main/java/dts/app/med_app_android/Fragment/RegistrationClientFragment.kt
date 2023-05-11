package dts.app.med_app_android.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dts.app.med_app_android.Model.RegisterClientRequest
import dts.app.med_app_android.Model.ReturnedToken
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.RoleManager
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.AuthService
import dts.app.med_app_android.Service.ClientService
import dts.app.med_app_android.databinding.RegistrationClientBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationClientFragment : Fragment() {
    private lateinit var binding: RegistrationClientBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var roleManager: RoleManager
    private lateinit var authService: AuthService
    private lateinit var clientService: ClientService
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RegistrationClientBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        roleManager = RoleManager(requireContext())
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        authService = retrofit.create(AuthService::class.java)
        clientService = retrofit.create(ClientService::class.java)
        register()
        spinner()
        return binding.root
    }

    private fun register() = with(binding) {
        buttonRegister.setOnClickListener {
            val callRegister = authService.registerClient(registerData())
            callRegister.enqueue(object : Callback<ReturnedToken> {
                override fun onResponse(
                    call: Call<ReturnedToken>,
                    response: Response<ReturnedToken>
                ) {
                    if (response.isSuccessful) {
                        val role = response.body()?.role
                        val token = response.body()?.token
                        roleManager.saveRole(role!!)
                        tokenManager.saveToken(token!!)
                        addCityToClient()
                        findNavController().navigate(R.id.homeFragment)
                    } else {
                        Log.i(
                            "Response Error",
                            "Failed to register client: ${response.code()} ${response.message()}"
                        )
                    }
                }

                override fun onFailure(call: Call<ReturnedToken>, t: Throwable) {
                    Log.i("Response Error", "Failed to register client: ${t.message}")
                }
            })
        }
    }

    private fun addCityToClient() = with(binding) {
        val cityId = getSelectedCityId(spinnerCity)
        val callAddCity = clientService.addCityToClient(cityId)
        callAddCity.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>, response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    Log.i("Response Good", "City added to client")
                } else {
                    Log.i(
                        "Response Error",
                        "Failed to add city to client: ${response.code()} ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i(
                    "Response Error", "Failed to add city to client: ${t.message}"
                )
            }
        })
    }

    private fun registerData(): RegisterClientRequest {
        return RegisterClientRequest(
            email = binding.inputEmail.text?.trim().toString(),
            password = binding.inputPassword.text?.trim().toString(),
            firstname = binding.inputFirstname.text?.trim().toString(),
            lastname = binding.inputLastname.text?.trim().toString()
        )
    }

    private fun getSelectedCityId(spinner: Spinner): Long {
        val position = spinner.selectedItemPosition
        return position + 1L
    }

    private fun spinner() = with(binding) {
        val cities = resources.getStringArray(R.array.cities)
        val cityAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, cities
        )
        spinnerCity.adapter = cityAdapter
    }


}