package com.dicoding.mindspace.factory

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mindspace.data.repository.ChatRepository
import com.dicoding.mindspace.di.Injection
import com.dicoding.mindspace.view.challenge.journal.JournalViewModel
import com.dicoding.mindspace.view.chat.ChatViewModel
import com.dicoding.mindspace.view.history.HistoryViewModel
import com.dicoding.mindspace.view.home.HomeViewModel
import com.dicoding.mindspace.view.start.ProfilingViewModel

class ViewModelWithTokenFactory(
    private val chatRepository: ChatRepository,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ChatViewModel::class.java) -> {
                ChatViewModel(chatRepository) as T
            }

            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(chatRepository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(chatRepository) as T
            }

            modelClass.isAssignableFrom(ProfilingViewModel::class.java) -> {
                ProfilingViewModel(chatRepository) as T
            }

            modelClass.isAssignableFrom(JournalViewModel::class.java) -> {
                JournalViewModel(chatRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context): ViewModelWithTokenFactory =
            ViewModelWithTokenFactory(Injection.provideChatRepository(context))
    }
}