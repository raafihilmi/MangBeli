package com.bumantra.mangbeli.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumantra.mangbeli.data.repository.MangRepository
import com.bumantra.mangbeli.di.Injection
import com.bumantra.mangbeli.ui.signup.SignUpViewModel

class ViewModelFactory(private val repository: MangRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository())
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}