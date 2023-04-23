package dts.app.med_app_android

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dts.app.med_app_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment()
    }

    private fun replaceFragment() = with(binding) {
        navController = findNavController(R.id.fragment_box)
        bottomNav.setupWithNavController(navController)

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> navController.navigate(R.id.homeFragment)
                R.id.item_add -> navController.navigate(R.id.addFragment)
                R.id.item_profile -> navController.navigate(R.id.profileClientFragment)
            }
            true
        }
    }

    fun hideBottomNavView() = with(binding) { bottomNav.visibility = View.GONE }

    fun showBottomNavView() = with(binding) { bottomNav.visibility = View.VISIBLE }
}