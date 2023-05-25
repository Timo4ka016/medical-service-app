package dts.app.med_app_android

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dts.app.med_app_android.Retrofit.RoleManager
import dts.app.med_app_android.Retrofit.TokenManager
import dts.app.med_app_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var tokenManager: TokenManager
    private lateinit var roleManager: RoleManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tokenManager = TokenManager(this)
        roleManager = RoleManager((this))
        navController = findNavController(R.id.fragment_box)
        bottomNavViewSettings()
        if (tokenManager.getToken() == null) {
            navigateToLogin()
        } else {
            updateBottomNavMenu()
        }
        setupBottomNavWithCustomListener()
        invalidateOptionsMenu()
    }

    private fun bottomNavViewSettings() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.login -> hideBottomNav()
                R.id.registrationClient -> hideBottomNav()
                R.id.registrationDoctor -> hideBottomNav()
                R.id.editProfileClient -> hideBottomNav()
                R.id.addFragment -> hideBottomNav()
                R.id.adDetailsFragment -> hideBottomNav()
                R.id.updateAdFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    private fun role(): String {
        return roleManager.getRole().toString()
    }

    private fun navigateToLogin() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.login, true)
            .build()
        navController.navigate(R.id.login, null, navOptions)
    }

    private fun setupBottomNavWithCustomListener() = with(binding) {
        bottomNav.setupWithNavController(navController)
        val customOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.item_home -> {
                        navController.navigate(R.id.homeFragment)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.item_add -> {
                        val role = roleManager.getRole()
                        if (role != null) {
                            when (role) {
                                "USER_CLIENT" -> {
                                    navController.navigate(R.id.adDetailsFragment)

                                }
                                "USER_DOCTOR" -> {
                                    navController.navigate(R.id.addFragment)

                                }
                            }
                        }
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.item_profile -> {
                        val role = roleManager.getRole()
                        if (role != null) {
                            when (role) {
                                "USER_CLIENT" -> navController.navigate(R.id.profileClientFragment)
                                "USER_DOCTOR" -> navController.navigate(R.id.profileDoctorFragment)
                            }
                        }
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }
        bottomNav.setOnItemSelectedListener(customOnNavigationItemSelectedListener)
    }

    private fun updateBottomNavMenu() {
        val role = roleManager.getRole()
        if (role != null) {
            when (role) {
                "USER_CLIENT" -> {
                    binding.bottomNav.menu.findItem(R.id.item_add)?.apply {
                        title = "Избранное"
                        setIcon(R.drawable.ic_favorite)
                    }
                }
                "USER_DOCTOR" -> {
                    binding.bottomNav.menu.findItem(R.id.item_add)?.apply {
                        title = "Создать"
                        setIcon(R.drawable.ic_add)
                    }
                }
            }
        }
    }

    private fun hideBottomNav() {
        binding.bottomNav.visibility = View.GONE
    }

    private fun showBottomNav() {
        binding.bottomNav.visibility = View.VISIBLE
    }
}