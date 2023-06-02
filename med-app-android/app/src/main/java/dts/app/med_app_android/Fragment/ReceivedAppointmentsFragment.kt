package dts.app.med_app_android.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dts.app.med_app_android.Adapter.AppointmentsAdapter
import dts.app.med_app_android.Model.MyAppointmentsDtoItem
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.DoctorService
import dts.app.med_app_android.databinding.FragmentReceivedAppointmentsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReceivedAppointmentsFragment : Fragment(), AppointmentsAdapter.OnAcceptClickListener,
    AppointmentsAdapter.OnRejectClickListener, AppointmentsAdapter.OnClickListener {
    private lateinit var binding: FragmentReceivedAppointmentsBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var doctorService: DoctorService
    private lateinit var adapter: AppointmentsAdapter
    private lateinit var btnLayout: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tokenManager = TokenManager(requireContext())
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        doctorService = retrofit.create(DoctorService::class.java)
        adapter = AppointmentsAdapter(this, this, this)
        binding = FragmentReceivedAppointmentsBinding.inflate(inflater, container, false)
        binding.rcAppointments.layoutManager = LinearLayoutManager(requireContext())
        binding.rcAppointments.adapter = adapter

        getMyAppointmentsByStatus("PENDING")
        filterBtn()
        return binding.root
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
                        adapter.submitList(appointmentList)
                    } else {
                        linearEmpty.visibility = View.GONE
                        adapter.submitList(appointmentList)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<MyAppointmentsDtoItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getMyAppointments() = with(binding) {
        val callGetMyAppointments = doctorService.getMyAppointments()
        callGetMyAppointments.enqueue(object : Callback<List<MyAppointmentsDtoItem>> {
            override fun onResponse(
                call: Call<List<MyAppointmentsDtoItem>>,
                response: Response<List<MyAppointmentsDtoItem>>
            ) {
                if (response.isSuccessful) {
                    val appointmentList = response.body()
                    if (appointmentList.isNullOrEmpty()) {
                        linearEmpty.visibility = View.VISIBLE
                    } else {
                        linearEmpty.visibility = View.GONE
                        adapter.submitList(appointmentList)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<MyAppointmentsDtoItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun acceptAppointment(appointmentId: Long) {
        val callAcceptAppointment = doctorService.acceptAppointment(appointmentId)
        callAcceptAppointment.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Принято", Toast.LENGTH_SHORT).show()
                    adapter.removeAppointmentById(appointmentId)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun rejectAppointment(appointmentId: Long) {
        val callRejectAppointment = doctorService.rejectAppointment(appointmentId)
        callRejectAppointment.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Отказано", Toast.LENGTH_SHORT).show()
                    adapter.removeAppointmentById(appointmentId)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onAcceptClick(appointmentId: Long) {
        acceptAppointment(appointmentId)
    }

    override fun onRejectClick(appointmentId: Long) {
        rejectAppointment(appointmentId)
    }

    override fun onClick(adId: Long) {
        val bundle = Bundle()
        bundle.putLong("adId", adId)
        findNavController().navigate(R.id.action_received_Appointments_to_adDetailsFragment, bundle)
    }
}