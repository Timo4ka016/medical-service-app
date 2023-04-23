package dts.app.med_app_android.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dts.app.med_app_android.databinding.HomeBinding

class Home: Fragment() {
    private lateinit var binding: HomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeBinding.inflate(inflater, container, false)


        return binding.root
    }
}