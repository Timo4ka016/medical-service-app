package dts.app.med_app_android.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dts.app.med_app_android.Adapter.DoctorAdsAdapter
import dts.app.med_app_android.Model.GetDoctorAdsItem
import dts.app.med_app_android.Model.GetMyAdsRequestItem
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.DoctorService
import dts.app.med_app_android.databinding.DoctorAdsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class DoctorAdsFragment : Fragment(), DoctorAdsAdapter.OnAdClickListener {
    private lateinit var binding: DoctorAdsBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var doctorService: DoctorService
    private lateinit var adapter: DoctorAdsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DoctorAdsBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        doctorService = retrofit.create(DoctorService::class.java)
        adapter = DoctorAdsAdapter(this)
        binding.rcMyAds.layoutManager = LinearLayoutManager(requireContext())
        binding.rcMyAds.adapter = adapter
        getMyAds()
        return binding.root
    }

    private fun getMyAds() = with(binding) {
        val callGetMyAds = doctorService.getMyAds()
        callGetMyAds.enqueue(object : Callback<List<GetDoctorAdsItem>> {
            override fun onResponse(
                call: Call<List<GetDoctorAdsItem>>,
                response: Response<List<GetDoctorAdsItem>>
            ) {
                if (response.isSuccessful) {
                    val adList = response.body()
                    adapter.submitList(adList)
                    Log.i("BRUH", response.body().toString())
                } else {
                    Log.i(
                        "Response Error",
                        "Failed to logout user: ${response.code()} ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<List<GetDoctorAdsItem>>, t: Throwable) {
                Log.i("Response Error", "Failed to get ads: ${t.message}")
            }
        })
    }

    override fun onAdClick(adId: Long) {
        val bundle = Bundle()
        bundle.putLong("adId", adId)

        findNavController().navigate(R.id.action_doctorAdsFragment_to_adDetailsFragment, bundle)
    }

}