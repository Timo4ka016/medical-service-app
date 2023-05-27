package dts.app.med_app_android.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import dts.app.med_app_android.Adapter.ClientFeedbacksOnAdDetailsAndDoctorProfileAdapter
import dts.app.med_app_android.Adapter.MyFeedbacksAdapter
import dts.app.med_app_android.Model.ClientMyFeedbacksItem
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.ClientService
import dts.app.med_app_android.databinding.FragmentMyFeedbacksBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFeedbacksFragment : Fragment(), MyFeedbacksAdapter.OnFeedbackClickListener {
    private lateinit var binding: FragmentMyFeedbacksBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var clientService: ClientService
    private lateinit var adapter: MyFeedbacksAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tokenManager = TokenManager(requireContext())
        adapter = MyFeedbacksAdapter(this)
        binding = FragmentMyFeedbacksBinding.inflate(inflater, container, false)
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        clientService = retrofit.create(ClientService::class.java)
        binding.rcFeedbacks.layoutManager = LinearLayoutManager(requireContext())
        binding.rcFeedbacks.adapter = adapter
        getMyFeedbacks()

        return binding.root
    }

    private fun getMyFeedbacks() {
        val callGetMyFeedbacks = clientService.getMyFeedbacks()
        callGetMyFeedbacks.enqueue(object : Callback<List<ClientMyFeedbacksItem>> {
            override fun onResponse(
                call: Call<List<ClientMyFeedbacksItem>>,
                response: Response<List<ClientMyFeedbacksItem>>
            ) {
                if (response.isSuccessful) {
                    val feedbacks = response.body()
                    adapter.submitList(feedbacks)
                }
            }

            override fun onFailure(call: Call<List<ClientMyFeedbacksItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onFeedbackClick(feedbackId: Long) {

    }
}