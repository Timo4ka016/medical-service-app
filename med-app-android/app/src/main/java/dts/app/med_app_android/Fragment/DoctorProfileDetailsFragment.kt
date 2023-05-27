package dts.app.med_app_android.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dts.app.med_app_android.Adapter.ClientFeedbacksOnAdDetailsAndDoctorProfileAdapter
import dts.app.med_app_android.Model.DoctorProfileInfoForClient
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.ClientService
import dts.app.med_app_android.databinding.DoctorProfileDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoctorProfileDetailsFragment : Fragment() {
    private lateinit var binding: DoctorProfileDetailsBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var clientService: ClientService
    private lateinit var adapter: ClientFeedbacksOnAdDetailsAndDoctorProfileAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DoctorProfileDetailsBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        clientService = retrofit.create(ClientService::class.java)
        adapter = ClientFeedbacksOnAdDetailsAndDoctorProfileAdapter()
        binding.rcFeedbacks.layoutManager = LinearLayoutManager(requireContext())
        binding.rcFeedbacks.adapter = adapter
        val doctorId = arguments?.getLong("doctorId")
        if (doctorId != null) {
            doctorProfileInfo(doctorId)
        }
        return binding.root
    }

    private fun doctorProfileInfo(doctorId: Long) = with(binding) {
        val callDoctorProfileDetails = clientService.getDoctorProfileInfoForClient(doctorId)
        callDoctorProfileDetails.enqueue(object : Callback<DoctorProfileInfoForClient> {
            override fun onResponse(
                call: Call<DoctorProfileInfoForClient>,
                response: Response<DoctorProfileInfoForClient>
            ) {
                if (response.isSuccessful) {
                    val doctor = response.body()
                    txtFullname.text = "Имя: " + doctor?.firstname + " " + doctor?.lastname
                    txtCityHeader.text = "Город: " + doctor?.city
                    txtDescription.text = doctor?.description
                    txtPhoneNumber.text = "Номер телефона: " + doctor?.phoneNumber.toString()
                    txtEmail.text = "Эл.почта: " + doctor?.email
                    adapter.submitList(doctor?.receivedFeedbacks)
                    Log.i("Response Good", response.body().toString())
                }
            }

            override fun onFailure(call: Call<DoctorProfileInfoForClient>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

}