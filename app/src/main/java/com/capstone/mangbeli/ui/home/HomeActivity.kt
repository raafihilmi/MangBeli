package com.capstone.mangbeli.ui.home


import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.mangbeli.R
import com.capstone.mangbeli.data.local.pref.SettingsPref
import com.capstone.mangbeli.data.local.pref.dataStore
import com.capstone.mangbeli.databinding.ActivityHomeBinding
import com.capstone.mangbeli.model.FCMToken
import com.capstone.mangbeli.ui.ViewModelFactory
import com.capstone.mangbeli.ui.settings.SettingViewModel
import com.capstone.mangbeli.utils.Result.Error
import com.capstone.mangbeli.utils.Result.Loading
import com.capstone.mangbeli.utils.Result.Success
import com.capstone.mygithubusers.ui.settings.SettingViewModelFactory
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var userRole: String? = null
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val tokenViewModel by viewModels<TokenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val navView: BottomNavigationView = binding.navView

        val pref = SettingsPref.getInstance(applicationContext.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Failed to get token", task.exception)
                    return@addOnCompleteListener
                }

                // Mendapatkan token
                val token = task.result
                Log.d("FCM", "Token: $token")
                val fcmToken = FCMToken(token)
                viewModel.updateFCMToken(fcmToken).observe(this){result ->
                    when (result) {
                        is Loading -> {
                            Log.d("FCM", "Loading Token: $result")
                        }
                        is Success -> {
                            Log.d("FCM", "Success Token: ${result.data.message}")
                        }
                        is Error -> {
                            Log.d("FCM", "Error Token: ${result.error}")
                        }
                    }
                }
            }

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        viewModel.getSession().observe(this) { user ->
            userRole = user.role
            val navController = findNavController(R.id.nav_host_fragment_activity_home)

            // Inflate navGraph
            val navGraph = navController.navInflater.inflate(R.navigation.mobile_navigation)
            // Set startDestination based on userRole
            when (userRole) {
                "user" -> {
                    navGraph.setStartDestination(R.id.navigation_home)
                }
                "vendor" -> {
                    navGraph.setStartDestination(R.id.navigation_home_pedagang)
                }

                else -> {
                    navGraph.setStartDestination(R.id.navigation_home)
                }
            }

            // Set navGraph to navController
            navController.graph = navGraph

            // Setup ActionBar dan NavigationController
            val appBarConfiguration = AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_maps, R.id.navigation_profile, R.id.navigation_settings
            ).build()
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

            navView.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.navigation_home -> {
                        when (userRole) {
                            "user" -> navController.navigate(R.id.navigation_home)
                            "vendor" -> navController.navigate(R.id.navigation_home_pedagang)
                            else -> navController.navigate(R.id.navigation_home)
                        }
                        true
                    }
                     R.id.navigation_maps -> {
                            navController.navigate(R.id.navigation_maps)
                         when (userRole) {
                             "user" -> navController.navigate(R.id.navigation_maps)
                             "vendor" -> navController.navigate(R.id.navigation_maps_pedagang)
                             else -> navController.navigate(R.id.navigation_maps)
                         }
                            true
                     }
                     R.id.navigation_profile -> {
                            navController.navigate(R.id.navigation_profile)
                         when (userRole) {
                             "user" -> navController.navigate(R.id.navigation_profile)
                             "vendor" -> navController.navigate(R.id.navigation_profile_pedagang)
                             else -> navController.navigate(R.id.navigation_profile)
                         }
                         true

                     }
                     R.id.navigation_settings -> {
                         when (userRole) {
                             "user" ->  navController.navigate(R.id.navigation_settings)
                             "vendor" ->  navController.navigate(R.id.navigation_settings)
                             else ->  navController.navigate(R.id.navigation_settings)
                         }
                            true
                     }
                    else -> false
                }
            }
        }
    }
}
