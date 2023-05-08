package dts.app.med_app_android_beta.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dts.app.med_app_android_beta.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    private lateinit var binding : FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addViewModel =
            ViewModelProvider(this).get(AddViewModel::class.java)
        binding = FragmentAddBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}