package com.dicoding.mindspace.view.start

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.mindspace.R
import com.dicoding.mindspace.data.pref.UserModel
import com.dicoding.mindspace.databinding.ActivityGreetingBinding
import com.dicoding.mindspace.factory.ViewModelWithoutTokenFactory
import com.dicoding.mindspace.view.MainActivity

class GreetingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGreetingBinding
    private var isProfiled: Boolean? = null
    private val viewModel by viewModels<GreetingViewModel> {
        ViewModelWithoutTokenFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        val nickname = intent.getStringExtra(EXTRA_NICKNAME)
        val pin = intent.getStringExtra(EXTRA_PIN)
        binding.tvGreeting.text = resources.getString(R.string.greeting_nickname, nickname)
        observeViewModel()
        viewModel.register(nickname.toString(), pin.toString())
    }

    private fun observeViewModel() {
        viewModel.registerData.observe(this) { data ->
            if (data.success == true) {
                isProfiled = data.data?.is_profiled
                viewModel.getMe(data.data?.token)
            }
        }

        viewModel.getSession().observe(this) { user ->
            if (user.isLogin || user.token.isNotEmpty()) {
                if (isProfiled != null && isProfiled == false) {
                    startActivity(Intent(this, MoodProfilingActivity::class.java))
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityGreetingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object {
        const val EXTRA_NICKNAME = "extra_nickname"
        const val EXTRA_PIN = "extra_pin"
    }
}