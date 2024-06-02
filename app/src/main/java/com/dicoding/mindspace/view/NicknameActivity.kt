package com.dicoding.mindspace.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.mindspace.R
import com.dicoding.mindspace.databinding.ActivityNicknameBinding

class NicknameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNicknameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupAction()
    }

    private fun setupAction() {
        val nicknameInput = binding.nicknameInput
        val nicknameBtn = binding.nicknameBtn
        nicknameBtn.isEnabled = false

        nicknameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nicknameBtn.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        nicknameBtn.setOnClickListener {
            val intent = Intent(this, PinActivity::class.java)
            intent.putExtra(PinActivity.NICKNAME_EXTRA, nicknameInput.text.toString())
            startActivity(intent)
        }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}