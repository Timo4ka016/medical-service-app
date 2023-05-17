package dts.app.med_app_android.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dts.app.med_app_android.Model.CreateAdRequest
import dts.app.med_app_android.Model.UpdateAdRequest
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.DoctorService
import dts.app.med_app_android.databinding.UpdateAdBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateAdFragment : Fragment() {
    private lateinit var binding: UpdateAdBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var doctorService: DoctorService
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UpdateAdBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        doctorService = retrofit.create(DoctorService::class.java)
        val adId = arguments?.getLong("adId", -1L) ?: -1L
        header()
        updateAd(adId)
        navigation()
        return binding.root
    }

    private fun updateAd(adId: Long) = with(binding) {
        btnUpdate.setOnClickListener {
            val selectedCategory = spinnerCategories.selectedItem.toString()
            if (selectedCategory == "Выберите категорию") {
                Toast.makeText(requireContext(), "Выберите категорию", Toast.LENGTH_SHORT).show()
            } else {
                val categoryId = spinnerCategories.selectedItemPosition.toLong()
                val adRequest = requestBody() ?: return@setOnClickListener
                val callUpdateAd = doctorService.updateAd(adId, categoryId, adRequest)
                callUpdateAd.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            findNavController().navigate(R.id.doctorAdsFragment, null)
                        } else {
                            Log.i(
                                "Response Error",
                                "Failed to update ad: ${response.code()} ${response.message()}"
                            )
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.i("Response Error", "Failed to update ad: ${t.message}")
                    }
                })
            }
        }
    }

    private fun header() = with(binding) {
        myHeader.relativeHeader.visibility = View.GONE
        myHeader.imgFavorite.visibility = View.GONE
        myHeader.txtPageName.visibility = View.VISIBLE
        myHeader.txtPageName.text = "Редактировать объявление"
        myHeader.imgBack.setOnClickListener {
            findNavController().navigate(R.id.adDetailsFragment)
        }
    }

    private fun requestBody(): UpdateAdRequest? {
        val title = binding.inputTitle.text?.trim().toString()
        val description = binding.inputDescription.text?.trim().toString()
        val address = binding.inputAddress.text?.trim().toString()
        val priceText = binding.inputPrice.text?.trim().toString()

        if (title.isEmpty() || description.isEmpty() || address.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            return null
        } else {
            val price: Long? = priceText.toLongOrNull()

            return UpdateAdRequest(
                title = title,
                description = description,
                address = address,
                price = price ?: 0L
            )
        }
    }

    private fun navigation() = with(binding) {
        btnCancel.setOnClickListener {
            findNavController().navigate(R.id.profileDoctorFragment)
        }
    }


}