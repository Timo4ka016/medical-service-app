package dts.app.med_app_android.Fragment

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import de.hdodenhof.circleimageview.CircleImageView
import dts.app.med_app_android.MainActivity
import dts.app.med_app_android.Model.AuthenticationRequest
import dts.app.med_app_android.Model.ReturnedToken
import dts.app.med_app_android.R
import dts.app.med_app_android.Retrofit.RetrofitClient
import dts.app.med_app_android.Retrofit.RoleManager
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.Service.AuthService
import dts.app.med_app_android.databinding.LoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Login : Fragment() {
    private lateinit var binding: LoginBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var roleManager: RoleManager
    private lateinit var authService: AuthService
    private lateinit var userTypeDialog: Dialog
    private lateinit var activity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginBinding.inflate(inflater, container, false)
        userTypeDialog = Dialog(requireContext())
        tokenManager = TokenManager(requireContext())
        roleManager = RoleManager(requireContext())
        val retrofit = RetrofitClient.getRetrofitClient(tokenManager)
        authService = retrofit.create(AuthService::class.java)
        login()
        showUserType()
        return binding.root
    }

    private fun showUserType() = with(binding) {
        txtRegister2.setOnClickListener {
            userTypeDialog()
        }
    }

    private fun userTypeDialog() {
        userTypeDialog.setContentView(R.layout.user_type_layout)
        userTypeDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        userTypeDialog.setCancelable(true)
        val typeDoctor: CircleImageView = userTypeDialog.findViewById(R.id.type_doctor)
        val typeClient: CircleImageView = userTypeDialog.findViewById(R.id.type_client)
        typeDoctor.setOnClickListener {
            userTypeDialog.dismiss()
            findNavController().navigate(R.id.registrationDoctor)
        }
        typeClient.setOnClickListener {
            userTypeDialog.dismiss()
            findNavController().navigate(R.id.registrationClient)
        }
        userTypeDialog.show()
    }

    private fun login() = with(binding) {
        buttonLogin.setOnClickListener {
            val callAuth = authService.authenticate(authenticationData())
            callAuth.enqueue(object : Callback<ReturnedToken> {
                override fun onResponse(
                    call: Call<ReturnedToken>,
                    response: Response<ReturnedToken>
                ) {
                    if (response.isSuccessful) {
                        val role = response.body()?.role
                        val token = response.body()?.token
                        roleManager.saveRole(role!!)
                        tokenManager.saveToken(token!!)
                    } else {
                        Log.i(
                            "Response Error",
                            "Failed to login user: ${response.code()} ${response.message()}"
                        )
                    }
                }

                override fun onFailure(call: Call<ReturnedToken>, t: Throwable) {
                    Log.i("Response Error", "Failed to login user: ${t.message}")
                }
            })
        }
    }

    private fun authenticationData(): AuthenticationRequest {
        return AuthenticationRequest(
            email = binding.inputEmail.text?.trim().toString(),
            password = binding.inputPassword.text?.trim().toString()
        )
    }

}