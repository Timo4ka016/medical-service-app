package dts.app.med_app_android.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dts.app.med_app_android.Adapter.ClientMySentAppointAdapter
import dts.app.med_app_android.Model.MyAppointmentsDtoItem
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.ClientService
import dts.app.med_app_android.databinding.FragmentMySentAppointmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MySentAppointmentFragment : Fragment(), ClientMySentAppointAdapter.OnClickListener {
private lateinit var binding: FragmentMySentAppointmentBinding
private lateinit var adapter: ClientMySentAppointAdapter
private lateinit var tokenManager: TokenManager
private lateinit var clientService: ClientService
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tokenManager = TokenManager(requireContext())
        adapter = ClientMySentAppointAdapter(this)
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        clientService = retrofit.create(ClientService::class.java)
        binding = FragmentMySentAppointmentBinding.inflate(inflater, container, false)
        binding.rcAppointments.layoutManager = LinearLayoutManager(requireContext())
        binding.rcAppointments.adapter = adapter

        filterBtn()
        getMyAppointmentsByStatus("PENDING")
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
        val callGetMyAppointments = clientService.getMyAppointmentsByStatus(status)
        callGetMyAppointments.enqueue(object: Callback<List<MyAppointmentsDtoItem>> {
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
                Log.i("Response error", t.message.toString())
            }
        })
    }

    override fun onClick(adId: Long) {
        val bundle = Bundle()
        bundle.putLong("adId", adId)
        findNavController().navigate(R.id.action_mySentAppointmentFragment_to_adDetailsFragment, bundle)
    }
}