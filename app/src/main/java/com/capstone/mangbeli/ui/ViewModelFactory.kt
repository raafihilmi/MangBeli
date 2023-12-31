package com.capstone.mangbeli.ui


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.mangbeli.data.repository.MangRepository
import com.capstone.mangbeli.di.Injection
import com.capstone.mangbeli.ui.detail.DetailViewModel
import com.capstone.mangbeli.ui.home.HomeViewModel
import com.capstone.mangbeli.ui.login.LoginViewModel
import com.capstone.mangbeli.ui.maps.MapsViewModel
import com.capstone.mangbeli.ui.pedagang.profile.ProfileVendorViewModel
import com.capstone.mangbeli.ui.profile.ProfileViewModel
import com.capstone.mangbeli.ui.role.AddRoleViewModel
import com.capstone.mangbeli.ui.signup.SignUpViewModel

class ViewModelFactory(private val repository: MangRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddRoleViewModel::class.java) -> {
                AddRoleViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddRoleViewModel::class.java) -> {
                AddRoleViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileVendorViewModel::class.java) -> {
                ProfileVendorViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
        fun refreshInstance() {
            INSTANCE = null
            Injection.refreshTokenRepository()
        }
    }

}