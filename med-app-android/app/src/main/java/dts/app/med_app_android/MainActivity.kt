package dts.app.med_app_android

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var tokenManager: TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tokenManager = TokenManager(this)
        navController = findNavController(R.id.fragment_box)

        if (tokenManager.getToken() == null) {
            navigateToLogin()
        } else {
            replaceFragment()
        }

    }

    private fun navigateToLogin() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.login, true)
            .build()
        navController.navigate(R.id.login, null, navOptions)
    }

    private fun replaceFragment() = with(binding) {
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