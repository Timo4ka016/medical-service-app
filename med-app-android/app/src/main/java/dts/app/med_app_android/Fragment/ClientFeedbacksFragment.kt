package dts.app.med_app_android.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.ClientService
import dts.app.med_app_android.databinding.ClientFeedbacksBinding

class ClientFeedbacksFragment: Fragment() {
    private lateinit var binding: ClientFeedbacksBinding
    private lateinit var clientService: ClientService
    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ClientFeedbacksBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        clientService = retrofit.create(ClientService::class.java)


        return binding.root
    }



}