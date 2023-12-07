package com.bumantra.mangbeli.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.bumantra.mangbeli.data.local.pref.SettingsPref
import com.bumantra.mangbeli.data.local.pref.dataStore
import com.bumantra.mangbeli.databinding.ActivitySplashScreenBinding
import com.bumantra.mangbeli.ui.MainActivity
import com.bumantra.mangbeli.ui.ViewModelFactory
import com.bumantra.mangbeli.ui.home.HomeActivity
import com.bumantra.mangbeli.ui.home.HomeViewModel
import com.bumantra.mygithubusers.ui.settings.SettingViewModel
import com.bumantra.mygithubusers.ui.settings.SettingViewModelFactory


@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val splashDisplayLength = 1000
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingsPref.getInstance(applicationContext.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        viewModel.getSession().observe(this){ user ->
            if (user.isLogin){
                Handler(Looper.getMainLooper()).postDelayed({
                    val mainIntent = Intent(this@SplashScreen, HomeActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                }, splashDisplayLength.toLong())
            }else{
                Handler(Looper.getMainLooper()).postDelayed({
                    val mainIntent = Intent(this@SplashScreen, MainActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                }, splashDisplayLength.toLong())

            }
        }
    }

    override fun onDestroy() {
        Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}