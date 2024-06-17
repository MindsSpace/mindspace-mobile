package com.dicoding.mindspace.view.chat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mindspace.R
import com.dicoding.mindspace.adapter.ChatAdapter
import com.dicoding.mindspace.databinding.FragmentChatBinding
import com.dicoding.mindspace.factory.ViewModelWithTokenFactory
import com.dicoding.mindspace.factory.ViewModelWithoutTokenFactory
import java.util.Locale

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val viewModel by viewModels<ChatViewModel> {
        ViewModelWithTokenFactory.getInstance(requireContext())
    }
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var room_id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        setupBackPressHandler()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        room_id = arguments?.getString(ROOM_ID) ?: ""
        setupAction()
        setupAdapter()
        observeViewModel()
        viewModel.getRoomById(room_id)
    }

    private fun setupAdapter() {
        chatAdapter = ChatAdapter()
        with(binding.rvChat) {
            val manager = LinearLayoutManager(requireContext())
            manager.stackFromEnd = true
            layoutManager = manager
            adapter = chatAdapter
        }

        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.rvChat.scrollToPosition(chatAdapter.itemCount - 1)
            }
        })
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setShowLoading(isLoading)
        }

        viewModel.responseMessage.observe(viewLifecycleOwner) { message ->
            setShowMessage(message)
        }

        viewModel.messages.observe(viewLifecycleOwner) { data ->
            chatAdapter.submitList(data)
            binding.rvChat.scrollToPosition(chatAdapter.itemCount - 1)
        }
    }

    private fun setupAction() {
        val sendBtn = binding.sendBtn
        val chatEditText = binding.chatEditText
        sendBtn.isEnabled = false

        // voice input
        binding.voiceInputBtn.setOnClickListener {
            startVoiceInput()
        }

        chatEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                toggleVoiceInputButton(s.isNullOrEmpty())
                sendBtn.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        toggleVoiceInputButton(chatEditText.text.isNullOrEmpty())

        // send chat
        binding.sendBtn.setOnClickListener {
            viewModel.sendMessage(chatEditText.text.toString(), room_id)
            chatEditText.text?.clear()
        }
    }

    private fun toggleVoiceInputButton(show: Boolean) {
        binding.voiceInputBtn.visibility = if (show) View.VISIBLE else View.GONE
        binding.divider.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.voice_input_prompt))
        try {
            launchSpeechRecognizer.launch(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val launchSpeechRecognizer =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val text = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (!text.isNullOrEmpty()) {
                    binding.chatEditText.setText(text[0])
                }
            }
        }

    private fun setShowMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setShowLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
    }

    companion object {
        const val ROOM_ID: String = "room_id"
    }
}