package dts.app.med_app_android_beta.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dts.app.med_app_android_beta.databinding.FragmentProfileDoctorBinding

class ProfileDoctorFragment : Fragment() {

    private lateinit var binding: FragmentProfileDoctorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileDoctorViewModel =
            ViewModelProvider(this).get(ProfileDoctorViewModel::class.java)

        binding = FragmentProfileDoctorBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}