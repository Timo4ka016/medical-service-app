package dts.app.med_app_android.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dts.app.med_app_android.MainActivity
import dts.app.med_app_android.R
import dts.app.med_app_android.databinding.EditProfileClientBinding


class EditProfileClient : Fragment() {
    private lateinit var binding: EditProfileClientBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditProfileClientBinding.inflate(inflater, container, false)
        header()
        navigation()
        return binding.root
    }


    private fun navigation() = with(binding) {
        myHeader.imgBack.setOnClickListener { findNavController().navigate(R.id.profileClientFragment) }
        btnSave.setOnClickListener { findNavController().navigate(R.id.profileClientFragment) }
    }

    private fun header() = with(binding) {
        myHeader.txtPageName.text = "Редактировать профиль"
    }

}