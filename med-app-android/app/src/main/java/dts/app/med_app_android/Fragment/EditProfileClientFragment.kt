package dts.app.med_app_android.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dts.app.med_app_android.Model.UpdateClientRequest
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.ClientService
import dts.app.med_app_android.databinding.EditProfileClientBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditProfileClientFragment : Fragment() {
    private lateinit var binding: EditProfileClientBinding
    private lateinit var clientService: ClientService
    private lateinit var tokenManager: TokenManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditProfileClientBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        clientService = retrofit.create(ClientService::class.java)
        header()
        navigation()
        updateClient()
        return binding.root
    }

    private fun updateClient() = with(binding) {
        btnSave.setOnClickListener {
            val callUpdateClient = clientService.updateClient(requestBody())
            callUpdateClient.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        findNavController().navigate(R.id.profileClientFragment, null)
                    } else {
                        Log.i(
                            "Response Error",
                            "Failed to update user: ${response.code()} ${response.message()}"
                        )
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.i("Response Error", "Failed to update user: ${t.message}")
                }
            })
        }
    }

    private fun requestBody(): UpdateClientRequest {
        return UpdateClientRequest(
            firstname = binding.inputFirstname.text?.trim().toString(),
            lastname = binding.inputLastname.text?.trim().toString(),
            password = binding.inputPassword.text?.trim().toString()
        )
    }

    private fun navigation() = with(binding) {
        myHeader.imgBack.setOnClickListener { findNavController().navigate(R.id.profileClientFragment) }
    }

    private fun header() = with(binding) {
        myHeader.relativeHeader.visibility = View.GONE
        myHeader.imgFavorite.visibility = View.GONE
        myHeader.txtPageName.visibility = View.VISIBLE
        myHeader.txtPageName.text = "Редактировать профиль"
    }

}