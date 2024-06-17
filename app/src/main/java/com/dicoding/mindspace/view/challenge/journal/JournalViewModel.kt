package com.dicoding.mindspace.view.challenge.journal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mindspace.data.remote.api.AnyApiResponse
import com.dicoding.mindspace.data.remote.api.GetAllJournalsResponse
import com.dicoding.mindspace.data.remote.api.GetJournalByIdResponse
import com.dicoding.mindspace.data.repository.ChatRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class JournalViewModel(private val repository: ChatRepository) : ViewModel() {
    private val _journal = MutableLiveData<GetJournalByIdResponse>()
    val journal: LiveData<GetJournalByIdResponse> = _journal

    private val _journals = MutableLiveData<GetAllJournalsResponse>()
    val journals: LiveData<GetAllJournalsResponse> = _journals

    private val _journalData = MutableLiveData<AnyApiResponse>()
    val journalData: LiveData<AnyApiResponse> = _journalData

    private val _deleteData = MutableLiveData<AnyApiResponse>()
    val deleteData: LiveData<AnyApiResponse> = _deleteData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingDelete = MutableLiveData<Boolean>()
    val isLoadingDelete: LiveData<Boolean> = _isLoadingDelete

    private val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String> = _responseMessage

    fun getAllJournals() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getAllJournals()
                if (response.success == false) {
                    _responseMessage.value = response.message ?: "Couldn't retrieve journals"
                } else {
                    _journals.value = response
                }
            } catch (e: Exception) {
                _responseMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getJournalById(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getJournalById(id)
                if (response.success == false) {
                    _responseMessage.value = response.message ?: "Couldn't get journal"
                } else {
                    _journal.value = response
                }
            } catch (e: Exception) {
                _responseMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteJournal(id: String) {
        _isLoadingDelete.value = true
        viewModelScope.launch {
            try {
                val response = repository.deleteJournal(id)
                if (response.success == false){
                    _responseMessage.value = response.message ?: "Couldn't delete journal"
                } else {
                    _deleteData.value = response
                }
            } catch (e: Exception) {
                _responseMessage.value = e.message
            } finally {
                _isLoadingDelete.value = false
            }
        }
    }

    fun createJournal(content: String, image: File?) {
        _isLoading.value = true
        val contentRequestBody = content.toRequestBody("text/plain".toMediaType())
        if (image != null) {
            val imageRequestBody = image.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                image.name,
                imageRequestBody
            )

            viewModelScope.launch {
                try {
                    val response = repository.createJournal(multipartBody, contentRequestBody)
                    if (response.success == false) {
                        _responseMessage.value = response.message ?: "Couldn't create journal"
                    } else {
                        _journalData.value = response
                    }
                } catch (e: Exception) {
                    _responseMessage.value = e.message
                } finally {
                    _isLoading.value = false
                }
            }
        } else {
            viewModelScope.launch {
                try {
                    val response = repository.createJournal(null, contentRequestBody)
                    if (response.success == false) {
                        _responseMessage.value = response.message ?: "Couldn't create journal"
                    } else {
                        _journalData.value = response
                    }
                } catch (e: Exception) {
                    _responseMessage.value = e.message
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }
}