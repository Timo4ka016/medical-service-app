package dts.app.med_app_android.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.RoleManager
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.AuthService
import dts.app.med_app_android.databinding.ProfileClientBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class ProfileClient : Fragment() {
    private lateinit var binding: ProfileClientBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var roleManager: RoleManager
    private lateinit var authService: AuthService
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileClientBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        roleManager = RoleManager(requireContext())
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        authService = retrofit.create(AuthService::class.java)
        navigate()

        return binding.root
    }

    private fun logout() = with(binding) {

        val callLogOut = authService.logout()
        callLogOut.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    tokenManager.removeToken()
                    roleManager.removeRole()
                    findNavController().navigate(R.id.login)
                } else {
                    Log.i(
                        "Response Error",
                        "Failed to logout user: ${response.code()} ${response.message()}"
                    )
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("Response Error", "Failed to logout user: ${t.message}")
            }
        })

    }

    private fun navigate() = with(binding) {
        btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.editProfileClient)
        }
        btnFavorite.setOnClickListener {
            findNavController().navigate(R.id.addFragment)
        }
        btnAppSettings.setOnClickListener {

        }
        btnFaq.setOnClickListener {

        }
        btnLogOut.setOnClickListener {
            logout()
        }
    }


}