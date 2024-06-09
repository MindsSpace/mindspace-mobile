package com.dicoding.mindspace.view.chat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.dicoding.mindspace.R
import com.dicoding.mindspace.databinding.FragmentChatBinding
import java.util.Locale

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val REQUEST_CODE_VOICE_INPUT = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.voiceInputBtn.setOnClickListener {
            startVoiceInput()
        }

        binding.chatEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                toggleVoiceInputButton(s.isNullOrEmpty())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        toggleVoiceInputButton(binding.chatEditText.text.isNullOrEmpty())
    }

    private fun toggleVoiceInputButton(show: Boolean) {
        binding.voiceInputBtn.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
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
                Log.d("speech recog", text.toString())
                if (!text.isNullOrEmpty()) {
                    binding.chatEditText.setText(text[0])
                }
            }
        }
}