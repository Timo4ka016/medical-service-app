package dts.app.med_app_android.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dts.app.med_app_android.Model.UpdateDoctorRequest
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.DoctorService
import dts.app.med_app_android.databinding.EditProfileDoctorBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileDoctorFragment : Fragment() {
    private lateinit var binding: EditProfileDoctorBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var doctorService: DoctorService
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditProfileDoctorBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        doctorService = retrofit.create(DoctorService::class.java)
        updateDoctor()
        navigation()
        header()
        return binding.root
    }

    private fun updateDoctor() = with(binding) {
        btnSave.setOnClickListener {
            val callUpdateDoctor = doctorService.updateDoctor(requestBody())
            callUpdateDoctor.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        findNavController().navigate(R.id.profileDoctorFragment, null)
                    } else {
                        Log.i(
                            "Response Error",
                            "Failed to update user: ${response.code()} ${response.message()}"
                        )
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.i("Response Error", "Failed to update user: ${t.message}")
                }
            })
        }
    }

    private fun requestBody(): UpdateDoctorRequest {
        val cityId = if (binding.spinnerCity.selectedItemPosition == 0) null else binding.spinnerCity.selectedItemPosition
        return UpdateDoctorRequest(
            firstname = binding.inputFirstname.text?.trim().toString(),
            lastname = binding.inputLastname.text?.trim().toString(),
            phoneNumber = binding.inputPhoneNumber.text?.trim().toString(),
            password = binding.inputPassword.text?.trim().toString(),
            description = binding.inputDescription.text?.trim().toString(),
            cityId = cityId?.toLong()
        )
    }

    private fun navigation() = with(binding) {
        myHeader.imgBack.setOnClickListener { findNavController().navigate(R.id.profileDoctorFragment) }
    }

    private fun header() = with(binding) {
        myHeader.relativeHeader.visibility = View.GONE
        myHeader.imgFavorite.visibility = View.GONE
        myHeader.txtPageName.visibility = View.VISIBLE
        myHeader.txtPageName.text = "Редактировать профиль"
    }

}