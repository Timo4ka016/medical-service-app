package dts.app.med_app_android.Fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dts.app.med_app_android.Adapter.ClientFeedbacksOnAdDetailsAndDoctorProfileAdapter
import dts.app.med_app_android.Model.FeedbackModel
import dts.app.med_app_android.Model.GetDoctorAdById
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.RoleManager
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.ClientService
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
    private lateinit var clientService: ClientService
    private lateinit var confirmDelete: Dialog
    private lateinit var createFeedback: Dialog
    private lateinit var adapter: ClientFeedbacksOnAdDetailsAndDoctorProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AdDetailsBinding.inflate(inflater, container, false)
        confirmDelete = Dialog(requireContext())
        createFeedback = Dialog(requireContext())
        roleManager = RoleManager(requireContext())
        tokenManager = TokenManager(requireContext())
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        adapter = ClientFeedbacksOnAdDetailsAndDoctorProfileAdapter()
        binding.rcFeedbacks.layoutManager = LinearLayoutManager(requireContext())
        binding.rcFeedbacks.adapter = adapter
        binding.rcFeedbacks.isNestedScrollingEnabled = false
        doctorService = retrofit.create(DoctorService::class.java)
        clientService = retrofit.create(ClientService::class.java)
        val adId = arguments?.getLong("adId", -1L) ?: -1L
        if (adId != -1L) {
            when (role()) {
                "USER_DOCTOR" -> {
                    getDoctorAdDetailInfo(adId)
                    setupDoctorButtons(adId)
                }

                "USER_CLIENT" -> {
                    getClientAdDetailInfo(adId)
                    setupClientButtons(adId)

                }
            }
        }
        return binding.root
    }

    private fun role(): String {
        return roleManager.getRole().toString()
    }

    private fun setupDoctorButtons(adId: Long) = with(binding) {
        btnFeedbackDialog.visibility = View.GONE
        btnGoProfile.visibility = View.GONE
        btnLeft2.text = getString(R.string.edit)
        btnLeft2.backgroundTintList = resources.getColorStateList(R.color.change_color)
        btnRight2.text = getString(R.string.delete)
        btnRight2.backgroundTintList = resources.getColorStateList(R.color.cancel_color)
        btnLeft2.setOnClickListener {
            val bundle = Bundle()
            bundle.putLong("adId", adId)
            findNavController().navigate(R.id.action_adDetailsFragment_to_updateAdFragment, bundle)
        }
        imgBack.setOnClickListener {
            findNavController().navigate(R.id.doctorAdsFragment)
        }
        btnRight2.setOnClickListener {
            confirmDeleteDialog(adId)
        }
    }

    private fun setupClientButtons(adId: Long) = with(binding) {
        btnLeft2.text = getString(R.string.call)
        btnLeft2.backgroundTintList = resources.getColorStateList(R.color.green_color)
        btnRight2.text = getString(R.string.in_favorite)
        btnRight2.backgroundTintList = resources.getColorStateList(R.color.cancel_color)
        imgBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        btnFeedbackDialog.setOnClickListener {
            createFeedbackDialog(adId)
        }
        btnRight2.setOnClickListener {
            addToFavorite(adId)
        }
        btnGoProfile.visibility = View.VISIBLE
    }

    private fun callNumber(number: String) = with(binding) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$number")
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun createFeedbackDialog(adId: Long) {
        createFeedback.setContentView(R.layout.create_feedback)
        createFeedback.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        createFeedback.setCancelable(true)
        createFeedback(adId)
        createFeedback.show()
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

    private fun getDoctorAdDetailInfo(adId: Long) = with(binding) {
        val callGetAdDetailInfo = doctorService.getDoctorAdById(adId)
        callGetAdDetailInfo.enqueue(object : Callback<GetDoctorAdById> {
            override fun onResponse(
                call: Call<GetDoctorAdById>,
                response: Response<GetDoctorAdById>
            ) {
                if (response.isSuccessful) {
                    val adDto = response.body()
                    val rating = String.format("%.2f", adDto?.rating)
                    txtTitle.text = adDto?.title
                    txtDescription.text = adDto?.description
                    txtCity.text = adDto?.city
                    txtAddress.text = adDto?.address
                    txtCategory.text = adDto?.category
                    txtRating.text = rating
                    txtPrice.text = adDto?.price.toString() + " KZT"
                    txtName.text = adDto?.doctor?.firstname + " " + adDto?.doctor?.lastname
                    txtPhoneNumber.text = adDto?.doctor?.phoneNumber
                    txtEmail.text = adDto?.doctor?.email
                    adapter.submitList(adDto?.doctor?.receivedFeedbacks)
                    Log.i("Response Good", response.body().toString())
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

    private fun createFeedback(adId: Long) = with(binding) {
        val btnSend = createFeedback.findViewById<MaterialButton>(R.id.btn_send)
        btnSend.setOnClickListener {
            val body = createFeedbackBody() ?: return@setOnClickListener
            val callCreateFeedback = clientService.createFeedback(adId, body)
            callCreateFeedback.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Спасибо за отзыв", Toast.LENGTH_SHORT)
                            .show()
                        createFeedback.dismiss()
                        getDoctorAdDetailInfo(adId)
                        Log.i("Response good", response.body().toString())
                    } else {
                        Log.i(
                            "Response Error",
                            "Failed to create feedback: ${response.code()} ${response.message()}"
                        )
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun createFeedbackBody(): FeedbackModel? {
        val ratingBar = createFeedback.findViewById<RatingBar>(R.id.rb_feedback_rating)
        val inputFeedbackText =
            createFeedback.findViewById<TextInputEditText>(R.id.input_feedback_text)
        val ratingText = ratingBar.rating.toString()
        val text = inputFeedbackText.text?.trim().toString()
        if (text.isEmpty() || ratingText.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            return null
        } else {
            val rating: Double = ratingText.toDouble()

            return FeedbackModel(
                text = text,
                rating = rating
            )
        }
    }

    private fun getClientAdDetailInfo(adId: Long) = with(binding) {
        val callGetAdDetailInfo = clientService.getAdById(adId)
        callGetAdDetailInfo.enqueue(object : Callback<GetDoctorAdById> {
            override fun onResponse(
                call: Call<GetDoctorAdById>,
                response: Response<GetDoctorAdById>
            ) {
                if (response.isSuccessful) {
                    Log.i("Response Good", response.body().toString())
                    val adDto = response.body()
                    val rating = String.format("%.2f", adDto?.rating)
                    txtTitle.text = adDto?.title
                    txtDescription.text = adDto?.description
                    txtCity.text = adDto?.city
                    txtAddress.text = adDto?.address
                    txtCategory.text = adDto?.category
                    txtRating.text = rating
                    txtPrice.text = adDto?.price.toString() + " KZT"
                    txtName.text = adDto?.doctor?.firstname + " " + adDto?.doctor?.lastname
                    txtPhoneNumber.text = adDto?.doctor?.phoneNumber
                    txtEmail.text = adDto?.doctor?.email
                    adapter.submitList(adDto?.doctor?.receivedFeedbacks)
                    btnLeft2.setOnClickListener {
                        callNumber(adDto?.doctor?.phoneNumber.toString())
                    }
                    btnGoProfile.setOnClickListener {
                        val doctorId = adDto?.doctor?.id
                        val bundle = Bundle()
                        bundle.putLong("doctorId", doctorId!!)
                        findNavController().navigate(
                            R.id.action_adDetailsFragment_to_doctorProfileDetailsFragment,
                            bundle
                        )
                    }
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

    private fun addToFavorite(adId: Long) = with(binding) {
        val callAddToFavorite = clientService.addToFavorite(adId)
        callAddToFavorite.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.i("Response Good", response.body().toString())
                    Toast.makeText(requireContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show()
                } else {
                    Log.i(
                        "Response Error",
                        "Failed to add ad to favorite: ${response.code()} ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("Response Error", "Failed to add ad to favorite: ${t.message}")
            }
        })
    }

}