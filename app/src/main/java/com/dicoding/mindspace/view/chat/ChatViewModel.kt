package com.dicoding.mindspace.view.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mindspace.data.remote.api.GetRoomByIdResponse
import com.dicoding.mindspace.data.remote.schema.Chat
import com.dicoding.mindspace.data.remote.schema.CreateChatRequest
import com.dicoding.mindspace.data.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {
    private val _roomData = MutableLiveData<GetRoomByIdResponse>()
    val roomData: LiveData<GetRoomByIdResponse> = _roomData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String> = _responseMessage

    private val _messages = MutableLiveData<List<Chat>?>()
    val messages: LiveData<List<Chat>?> = _messages

    fun getRoomById(id: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getRoomById(id)
                if (response.success == false) {
                    _responseMessage.value = response.message ?: "Couldn't retrieve chat room"
                } else {
                    _roomData.value = response
                    _messages.value = response.data?.chats
                }
            } catch (e: Exception) {
                _responseMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sendMessage(content: String, roomId: String) {
        val tempId = generateTempId()
        val userMessage = Chat(
            id = tempId,
            content = content,
            is_user = true,
            room_id = roomId,
            isLoading = false
        )
        val botLoadingMessage = Chat(
            id = "bot_loading_$tempId",
            is_user = false,
            room_id = roomId,
            isLoading = true
        )

        _messages.value = _messages.value?.plus(userMessage)
        _messages.value = messages.value?.plus(botLoadingMessage)

        viewModelScope.launch {
            try {
                val request = CreateChatRequest(content = content, room_id = roomId)
                val response = repository.sendChat(request)
                if (response.success == false) {
                    _responseMessage.value = response.message ?: "Couldn't send chat"
                } else {
                    val bot = response.data
                    _messages.value = _messages.value?.filterNot { it.id == "bot_loading_$tempId" && it.isLoading }
                    bot?.let {
                        _messages.value = _messages.value?.plus(it)
                    }
                }
            } catch (e: Exception) {
                _responseMessage.value = e.message
            }
        }
    }

    private fun generateTempId(): String {
        return "temp_${System.currentTimeMillis()}"
    }
}
