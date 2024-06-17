package com.dicoding.mindspace.view.loading

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.mindspace.R
import com.dicoding.mindspace.databinding.ActivityLoadingBinding
import com.dicoding.mindspace.factory.ViewModelWithoutTokenFactory
import com.dicoding.mindspace.view.MainActivity
import com.dicoding.mindspace.view.start.MoodProfilingActivity
import com.dicoding.mindspace.view.start.StartActivity

class LoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding
    private val viewModel by viewModels<LoadingViewModel> {
        ViewModelWithoutTokenFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setupView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getSession().observe(this) { session ->
            if (session.token.isNotEmpty() && session.isLogin) {
                viewModel.getMe(session.token)
            } else {
                val intent = Intent(this, StartActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        viewModel.meData.observe(this) { me ->
            if(me.success == true) {
                if (me.data?.is_profiled == false) {
                    val intent = Intent(this, MoodProfilingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }

        viewModel.responseMessage.observe(this) { message ->
            setShowMessage(message)
        }
    }

    private fun setShowMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }
}