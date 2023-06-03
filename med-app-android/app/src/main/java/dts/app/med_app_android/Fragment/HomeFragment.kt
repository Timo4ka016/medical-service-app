package dts.app.med_app_android.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dts.app.med_app_android.Adapter.AppointmentsAdapter
import dts.app.med_app_android.Adapter.DoctorAdsAdapter
import dts.app.med_app_android.Adapter.DoctorReceivedFeedbacksAdapter
import dts.app.med_app_android.Model.ClientMyFeedbacksItem
import dts.app.med_app_android.Model.FavoriteAdsDtoItem
import dts.app.med_app_android.Model.FeedbackDto
import dts.app.med_app_android.Model.GetDoctorAdsItem
import dts.app.med_app_android.Model.MyAppointmentsDtoItem
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.RoleManager
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.ClientService
import dts.app.med_app_android.Service.DoctorService
import dts.app.med_app_android.databinding.HomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), DoctorAdsAdapter.OnAdClickListener,
    AppointmentsAdapter.OnClickListener, AppointmentsAdapter.OnAcceptClickListener,
    AppointmentsAdapter.OnRejectClickListener {
    private lateinit var binding: HomeBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var roleManager: RoleManager
    private lateinit var clientService: ClientService
    private lateinit var doctorAdsAdapter: DoctorAdsAdapter
    private lateinit var appointmentsAdapter: AppointmentsAdapter
    private lateinit var feedbacksAdapter: DoctorReceivedFeedbacksAdapter
    private lateinit var doctorService: DoctorService
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        roleManager = RoleManager(requireContext())
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        clientService = retrofit.create(ClientService::class.java)
        doctorService = retrofit.create(DoctorService::class.java)
        appointmentsAdapter = AppointmentsAdapter(this, this, this)
        feedbacksAdapter = DoctorReceivedFeedbacksAdapter()
        doctorAdsAdapter = DoctorAdsAdapter(this)
        binding.rcAppointments.layoutManager = LinearLayoutManager(requireContext())
        binding.rcAppointments.adapter = appointmentsAdapter
        binding.rcRecAds.layoutManager = LinearLayoutManager(requireContext())
        binding.rcRecAds.adapter = doctorAdsAdapter
        binding.rcFeedback.layoutManager = LinearLayoutManager(requireContext())
        binding.rcFeedback.adapter = feedbacksAdapter

        when (role()) {
            "USER_DOCTOR" -> {
                binding.constraintDoctor.visibility = View.VISIBLE
                binding.constraintUser.visibility = View.GONE
                getMyAppointmentsByStatus("PENDING")
                filterBtn()
                getMyFeedbacks()
            }

            "USER_CLIENT" -> {
                binding.constraintUser.visibility = View.VISIBLE
                binding.constraintDoctor.visibility = View.GONE
                getRecommendations()
                selectedCard()
                header()
            }
        }
        return binding.root
    }

    private fun header() = with(binding) {
        myHeader.imgBack.visibility = View.GONE
        myHeader.txtPageName.visibility = View.GONE
        myHeader.relativeHeader.visibility = View.VISIBLE
    }

    private fun role(): String {
        return roleManager.getRole().toString()
    }

    private fun selectedCard() {
        binding.card1.setOnClickListener {
            getAdByCategory(1)
        }
        binding.card2.setOnClickListener {
            getAdByCategory(2)
        }
        binding.card3.setOnClickListener {
            getAdByCategory(3)
        }
        binding.card4.setOnClickListener {
            getAdByCategory(4)
        }
        binding.card5.setOnClickListener {
            getAdByCategory(5)
        }
    }

    private fun filterBtn() = with(binding) {
        btnPending.setOnClickListener {
            getMyAppointmentsByStatus("PENDING")

        }
        btnAccepted.setOnClickListener {
            getMyAppointmentsByStatus("ACCEPTED")
        }
        btnRejected.setOnClickListener {
            getMyAppointmentsByStatus("REJECTED")
        }
    }

    private fun getMyFeedbacks() = with(binding) {
        val callGetMyFeedbacks = doctorService.getMyFeedbacks()
        callGetMyFeedbacks.enqueue(object : Callback<List<ClientMyFeedbacksItem>> {
            override fun onResponse(
                call: Call<List<ClientMyFeedbacksItem>>,
                response: Response<List<ClientMyFeedbacksItem>>
            ) {
                if (response.isSuccessful) {
                    val feedbacks = response.body()
                    if (feedbacks.isNullOrEmpty()) {
                        linearFeedbackEmpty.visibility = View.VISIBLE
                        feedbacksAdapter.submitList(feedbacks)
                    } else {
                        linearFeedbackEmpty.visibility = View.GONE
                        Log.i("Response feedback good", response.body().toString())
                        feedbacksAdapter.submitList(feedbacks)
                    }

                } else {
                    Log.i("Response feedback bad", response.body().toString())
                }
            }

            override fun onFailure(call: Call<List<ClientMyFeedbacksItem>>, t: Throwable) {
                Log.i("Response error", t.message.toString())
            }
        })
    }

    private fun getMyAppointmentsByStatus(status: String) = with(binding) {
        val callGetMyAppointmentsByStatus = doctorService.getMyAppointmentsByStatus(status)
        callGetMyAppointmentsByStatus.enqueue(object : Callback<List<MyAppointmentsDtoItem>> {
            override fun onResponse(
                call: Call<List<MyAppointmentsDtoItem>>,
                response: Response<List<MyAppointmentsDtoItem>>
            ) {
                if (response.isSuccessful) {
                    val appointmentList = response.body()
                    if (appointmentList.isNullOrEmpty()) {
                        linearEmpty.visibility = View.VISIBLE
                        appointmentsAdapter.submitList(appointmentList)
                    } else {
                        linearEmpty.visibility = View.GONE
                        appointmentsAdapter.submitList(appointmentList)
                        appointmentsAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<MyAppointmentsDtoItem>>, t: Throwable) {
                Log.i("Response error", t.message.toString())
            }
        })
    }

    private fun acceptAppointment(appointmentId: Long) {
        val callAcceptAppointment = doctorService.acceptAppointment(appointmentId)
        callAcceptAppointment.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Принято", Toast.LENGTH_SHORT).show()
                    appointmentsAdapter.removeAppointmentById(appointmentId)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("Response error", t.message.toString())
            }
        })
    }

    private fun rejectAppointment(appointmentId: Long) {
        val callRejectAppointment = doctorService.rejectAppointment(appointmentId)
        callRejectAppointment.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Отказано", Toast.LENGTH_SHORT).show()
                    appointmentsAdapter.removeAppointmentById(appointmentId)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("Response error", t.message.toString())
            }
        })
    }

    private fun getAdByCategory(categoryId: Long) = with(binding) {
        val callGetAdByCategory = clientService.getAdByCategory(categoryId)
        callGetAdByCategory.enqueue(object : Callback<List<GetDoctorAdsItem>> {
            override fun onResponse(
                call: Call<List<GetDoctorAdsItem>>,
                response: Response<List<GetDoctorAdsItem>>
            ) {
                if (response.isSuccessful) {
                    val adList = response.body()
                    doctorAdsAdapter.submitList(adList)
                    Log.i("Response good", response.body().toString())
                } else {
                    Log.i(
                        "Response Error",
                        "Failed to get ads with category: ${response.code()} ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<List<GetDoctorAdsItem>>, t: Throwable) {
                Log.i("Response Error", "Failed to get ads with category: ${t.message}")
            }
        })
    }

    private fun getRecommendations() = with(binding) {
        val callGetRecommendations = clientService.getRecommendations()
        callGetRecommendations.enqueue(object : Callback<List<GetDoctorAdsItem>> {
            override fun onResponse(
                call: Call<List<GetDoctorAdsItem>>,
                response: Response<List<GetDoctorAdsItem>>
            ) {
                if (response.isSuccessful) {
                    val adList = response.body()
                    doctorAdsAdapter.submitList(adList)
                    Log.i("Response good", response.body().toString())
                } else {
                    Log.i(
                        "Response Error",
                        "Failed to get в recommendations: ${response.code()} ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<List<GetDoctorAdsItem>>, t: Throwable) {
                Log.i("Response Error", "Failed to get recommendations: ${t.message}")
            }
        })
    }


    override fun onAdClick(adId: Long) {
        val bundle2 = Bundle()
        bundle2.putLong("adId", adId)
        findNavController().navigate(R.id.action_homeFragment_to_adDetailsFragment, bundle2)
    }

    override fun onClick(adId: Long) {
        val bundle = Bundle()
        bundle.putLong("adId", adId)
        findNavController().navigate(R.id.action_homeFragment_to_adDetailsFragment, bundle)

    }

    override fun onAcceptClick(appointmentId: Long) {
        acceptAppointment(appointmentId)
    }

    override fun onRejectClick(appointmentId: Long) {
        rejectAppointment(appointmentId)
    }
}