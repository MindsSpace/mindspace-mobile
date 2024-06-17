package com.dicoding.mindspace.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mindspace.data.repository.UserRepository
import com.dicoding.mindspace.di.Injection
import com.dicoding.mindspace.view.MainViewModel
import com.dicoding.mindspace.view.loading.LoadingViewModel
import com.dicoding.mindspace.view.start.GreetingViewModel

class ViewModelWithoutTokenFactory(
    private val userRepository: UserRepository,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(GreetingViewModel::class.java) -> {
                GreetingViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(LoadingViewModel::class.java) -> {
                LoadingViewModel(userRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelWithoutTokenFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelWithoutTokenFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelWithoutTokenFactory::class.java) {
                    INSTANCE = ViewModelWithoutTokenFactory(
                        userRepository = Injection.provideUserRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelWithoutTokenFactory
        }
    }
}