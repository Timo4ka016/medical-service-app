package dts.app.med_app_android.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dts.app.med_app_android.Adapter.FavoriteAdsAdapter
import dts.app.med_app_android.Model.FavoriteAdsDtoItem
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.ClientService
import dts.app.med_app_android.databinding.FragmentFavoriteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteFragment : Fragment(), FavoriteAdsAdapter.OnDeleteClickListener,
    FavoriteAdsAdapter.OnGoProfileClickListener, FavoriteAdsAdapter.OnAdClickListener {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var clientService: ClientService
    private lateinit var adapter: FavoriteAdsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tokenManager = TokenManager(requireContext())
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        clientService = retrofit.create(ClientService::class.java)
        adapter = FavoriteAdsAdapter(this, this, this)
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding.rcFeedbacks.layoutManager = LinearLayoutManager(requireContext())
        binding.rcFeedbacks.adapter = adapter
        getAllFavorite()
        return binding.root
    }

    private fun getAllFavorite() = with(binding) {
        val callGetAllFavorite = clientService.getAllFavorite()
        callGetAllFavorite.enqueue(object : Callback<List<FavoriteAdsDtoItem>> {
            override fun onResponse(
                call: Call<List<FavoriteAdsDtoItem>>,
                response: Response<List<FavoriteAdsDtoItem>>
            ) {
                if (response.isSuccessful) {
                    val favoriteAds = response.body()
                    Log.i("Response good", response.body().toString())
                    if (favoriteAds.isNullOrEmpty()) {
                        linearEmpty.visibility = View.VISIBLE
                    } else {
                        linearEmpty.visibility = View.GONE
                        adapter.submitList(favoriteAds)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Log.i(
                        "Response Error",
                        "Failed to get favorite ads list: ${response.code()} ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<List<FavoriteAdsDtoItem>>, t: Throwable) {
                Log.i("Response Error", "Failed to get favorite ads list: ${t.message}")
            }
        })
    }

    private fun deleteFavoriteAd(adId: Long) = with(binding) {
        val callDeleteFavoriteAd = clientService.deleteFavoriteAd(adId)
        callDeleteFavoriteAd.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    adapter.removeFavoriteAdById(adId)
                    getAllFavorite()
                    Log.i("Response good", response.body().toString())
                } else {
                    Log.i(
                        "Response Error",
                        "Failed to delete favorite ad: ${response.code()} ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("Response Error", "Failed to delete favorite ad: ${t.message}")
            }
        })
    }

    override fun onDeleteClick(adId: Long) {
        deleteFavoriteAd(adId)
    }

    override fun onGoProfileClick(doctorId: Long) {
        val bundle = Bundle()
        bundle.putLong("doctorId", doctorId)
        findNavController().navigate(R.id.action_favoriteFragment_to_doctorProfileDetailsFragment, bundle)
    }

    override fun onAdClick(adId: Long) {
        val bundle = Bundle()
        bundle.putLong("adId", adId)
        findNavController().navigate(R.id.action_favoriteFragment_to_adDetailsFragment, bundle)
    }
}