package dts.app.med_app_android.Fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import dts.app.med_app_android.Model.GetDoctorAdById
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.RoleManager
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.DoctorService
import dts.app.med_app_android.databinding.AdDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdDetailsFragment : Fragment() {
    private lateinit var binding: AdDetailsBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var roleManager: RoleManager
    private lateinit var doctorService: DoctorService
    private lateinit var confirmDelete: Dialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AdDetailsBinding.inflate(inflater, container, false)
        confirmDelete = Dialog(requireContext())
        roleManager = RoleManager(requireContext())
        tokenManager = TokenManager(requireContext())
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        doctorService = retrofit.create(DoctorService::class.java)
        val adId = arguments?.getLong("adId", -1L) ?: -1L
        if (adId != -1L) {
            getAdDetailInfo(adId)
            btnSettings(adId)
        }

        return binding.root
    }

    private fun btnSettings(adId: Long) = with(binding) {
        val role = roleManager.getRole()
        when (role) {
            "USER_DOCTOR" -> {
                btnLeft.text = getString(R.string.edit)
                btnLeft.backgroundTintList = resources.getColorStateList(R.color.change_color)
                btnRight.text = getString(R.string.delete)
                btnRight.backgroundTintList = resources.getColorStateList(R.color.cancel_color)
                btnLeft.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putLong("adId", adId)
                    findNavController().navigate(R.id.action_adDetailsFragment_to_updateAdFragment, bundle)
                }
                imgBack.setOnClickListener {
                    findNavController().navigate(R.id.doctorAdsFragment)
                }
                btnRight.setOnClickListener {
                    confirmDeleteDialog(adId)
                }
            }
            "USER_CLIENT" -> {
                btnLeft.text = getString(R.string.call)
                btnLeft.backgroundTintList = resources.getColorStateList(R.color.green_color)
                btnRight.text = getString(R.string.in_favorite)
                btnRight.backgroundTintList = resources.getColorStateList(R.color.cancel_color)
            }
        }
    }

    private fun confirmDeleteDialog(adId: Long) {
        confirmDelete.setContentView(R.layout.confirm_card)
        confirmDelete.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        confirmDelete.setCancelable(true)
        val btnNo: MaterialButton = confirmDelete.findViewById(R.id.btn_no)
        val btnYes: MaterialButton = confirmDelete.findViewById(R.id.btn_yes)
        btnNo.setOnClickListener {
            confirmDelete.dismiss()
        }
        btnYes.setOnClickListener {
            confirmDelete.dismiss()
            deleteAd(adId)
        }
        confirmDelete.show()
    }

    private fun deleteAd(adId: Long) = with(binding) {
        val callDeleteAd = doctorService.deleteAd(adId)
        callDeleteAd.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    findNavController().navigate(R.id.doctorAdsFragment)
                } else {
                    Log.i(
                        "Response Error",
                        "Failed to delete ad: ${response.code()} ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("Response Error", "Failed to delete ad: ${t.message}")
            }
        })

    }

    private fun getAdDetailInfo(adId: Long) = with(binding) {
        val callGetAdDetailInfo = doctorService.getDoctorAdById(adId)
        callGetAdDetailInfo.enqueue(object : Callback<GetDoctorAdById> {
            override fun onResponse(
                call: Call<GetDoctorAdById>,
                response: Response<GetDoctorAdById>
            ) {
                if (response.isSuccessful) {
                    val adDto = response.body()
                    txtTitle.text = adDto?.title
                    txtDescription.text = adDto?.description
                    txtCity.text = adDto?.city
                    txtAddress.text = adDto?.address
                    txtCategory.text = adDto?.category
                    txtRating.text = adDto?.rating.toString()
                    txtPrice.text = adDto?.price.toString() + " KZT"
                    txtName.text = adDto?.doctor?.firstname + " " + adDto?.doctor?.lastname
                    txtPhoneNumber.text = adDto?.doctor?.phoneNumber
                    txtEmail.text = adDto?.doctor?.email
                } else {
                    Log.i(
                        "Response Error",
                        "Failed to get ad details: ${response.code()} ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<GetDoctorAdById>, t: Throwable) {
                Log.i("Response Error", "Failed to get ad details: ${t.message}")
            }
        })
    }

}