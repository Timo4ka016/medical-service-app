package dts.app.med_app_android.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dts.app.med_app_android.Model.RegisterDoctorRequest
import dts.app.med_app_android.Model.ReturnedToken
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.RoleManager
import dts.app.med_app_android.Service.AuthService
import dts.app.med_app_android.Service.DoctorService
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.databinding.RegistrationDoctorBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationDoctorFragment : Fragment() {
    private lateinit var binding: RegistrationDoctorBinding
    private lateinit var doctorService: DoctorService
    private lateinit var authService: AuthService
    private lateinit var tokenManager: TokenManager
    private lateinit var roleManager: RoleManager
    private val selectedCategoryIds = mutableListOf<Long>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = RegistrationDoctorBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        roleManager = RoleManager(requireContext())
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        authService = retrofit.create(AuthService::class.java)
        doctorService = retrofit.create(DoctorService::class.java)
        setupCategoryButtons()
        spinner()
        register()
        return binding.root
    }

    private fun register() = with(binding) {
        navigateToFirstFormPart()
        navigateToSecondFormPart()
        buttonRegister.setOnClickListener {

            val callRegister = authService.registerDoctor(registerData())
            callRegister.enqueue(object : Callback<ReturnedToken> {
                override fun onResponse(
                    call: Call<ReturnedToken>, response: Response<ReturnedToken>
                ) {
                    if (response.isSuccessful) {
                        val role = response.body()?.role
                        val token = response.body()?.token
                        roleManager.saveRole(role!!)
                        tokenManager.saveToken(token!!)
                        addCategoryToDoctor()
                        addCityToDoctor()
                        findNavController().navigate(R.id.homeFragment)
                    } else {
                        Log.i(
                            "Response Error",
                            "Failed to register doctor: ${response.code()} ${response.message()}"
                        )
                    }
                }

                override fun onFailure(call: Call<ReturnedToken>, t: Throwable) {
                    Log.i("Response Error", "Failed to register doctor: ${t.message}")
                }
            })
        }
    }

    private fun navigateToSecondFormPart() = with(binding) {
        buttonContinue.setOnClickListener {
            registrationFirstPart.visibility = View.GONE
            buttonContinue.visibility = View.GONE
            buttonBack.visibility = View.VISIBLE
            buttonRegister.visibility = View.VISIBLE
            registrationSecondPart.visibility = View.VISIBLE
        }
    }

    private fun navigateToFirstFormPart() = with(binding) {
        buttonBack.setOnClickListener {
            registrationFirstPart.visibility = View.VISIBLE
            buttonContinue.visibility = View.VISIBLE
            buttonBack.visibility = View.GONE
            buttonRegister.visibility = View.GONE
            registrationSecondPart.visibility = View.GONE
        }
    }

    private fun addCategoryToDoctor() = with(binding) {
        val callAddCategory = doctorService.addCategoryToDoctor(selectedCategoryIds)
        callAddCategory.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>, response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    Log.i("Response Good", tokenManager.getToken().toString())
                } else {
                    Log.i(
                        "Response Error",
                        "Failed to add city to doctor: ${response.code()} ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i(
                    "Response Error", "Failed to add city to doctor: ${t.message}"
                )
            }
        })
    }

    private fun addCityToDoctor() = with(binding) {
        val cityId = getSelectedCityId(spinnerCity)
        val callAddCity = doctorService.addCityToDoctor(cityId)
        callAddCity.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>, response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    Log.i("Response Good", "City added to doctor")
                } else {
                    Log.i(
                        "Response Error",
                        "Failed to add city to doctor: ${response.code()} ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i(
                    "Response Error", "Failed to add city to doctor: ${t.message}"
                )
            }
        })
    }

    private fun registerData(): RegisterDoctorRequest {
        val registerDoctor = RegisterDoctorRequest(
            email = binding.inputEmail.text?.trim().toString(),
            password = binding.inputPassword.text?.trim().toString(),
            firstname = binding.inputFirstname.text?.trim().toString(),
            lastname = binding.inputLastname.text?.trim().toString(),
            phoneNumber = binding.inputPhoneNumber.text?.trim().toString()
        )
        return registerDoctor
    }

    private fun getSelectedCityId(spinner: Spinner): Long {
        val position = spinner.selectedItemPosition
        return position + 1L
    }

    private fun setupCategoryButtons() = with(binding) {
        val categoryButtons = arrayOf(
            buttonCategory1, buttonCategory2, buttonCategory3, buttonCategory4, buttonCategory5
        )
        for ((index, button) in categoryButtons.withIndex()) {
            button.setOnClickListener {
                val categoryId = getCategoryById(index)
                if (selectedCategoryIds.contains(categoryId)) {
                    selectedCategoryIds.remove(categoryId)
                    button.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.white
                        )
                    )
                } else {
                    selectedCategoryIds.add(categoryId)
                    button.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.purple_200
                        )
                    )
                }
            }
        }
    }

    private fun getCategoryById(index: Int): Long {
        return index + 1L
    }

    private fun spinner() = with(binding) {
        val cities = resources.getStringArray(R.array.cities)
        val cityAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, cities
        )
        spinnerCity.adapter = cityAdapter
    }


}