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
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.RoleManager
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.ClientService
import dts.app.med_app_android.databinding.HomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), DoctorAdsAdapter.OnAdClickListener {
    private lateinit var binding: HomeBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var roleManager: RoleManager
    private lateinit var clientService: ClientService
    private lateinit var adapter: DoctorAdsAdapter
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
        adapter = DoctorAdsAdapter(this)
        binding.rcRecAds.layoutManager = LinearLayoutManager(requireContext())
        binding.rcRecAds.adapter = adapter
        when (role()) {
            "USER_DOCTOR" -> true
            "USER_CLIENT" -> {
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

    private fun getAdByCategory(categoryId: Long) = with(binding) {
        val callGetAdByCategory = clientService.getAdByCategory(categoryId)
        callGetAdByCategory.enqueue(object : Callback<List<GetDoctorAdsItem>> {
            override fun onResponse(
                call: Call<List<GetDoctorAdsItem>>,
                response: Response<List<GetDoctorAdsItem>>
            ) {
                if (response.isSuccessful) {
                    val adList = response.body()
                    adapter.submitList(adList)
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
                    adapter.submitList(adList)
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
        val bundle = Bundle()
        bundle.putLong("adId", adId)

        findNavController().navigate(R.id.action_homeFragment_to_adDetailsFragment, bundle)
    }

}