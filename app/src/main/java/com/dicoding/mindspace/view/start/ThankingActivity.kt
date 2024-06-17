package com.dicoding.mindspace.view.start

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.dicoding.mindspace.R
import com.dicoding.mindspace.data.remote.schema.ProfilingRequest
import com.dicoding.mindspace.databinding.ActivityThankingBinding
import com.dicoding.mindspace.factory.ViewModelWithTokenFactory
import com.dicoding.mindspace.view.MainActivity
import com.dicoding.mindspace.view.chat.ChatFragment

class ThankingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThankingBinding
    private val viewModel by viewModels<ProfilingViewModel> {
        ViewModelWithTokenFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        val request = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<ProfilingRequest>(
                EXTRA_PROFILING,
                ProfilingRequest::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<ProfilingRequest>(EXTRA_PROFILING)
        }
        if (request != null) {
            viewModel.createProfile(request)
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.profilingData.observe(this) { data ->
            val roomId = data.data?.room_id
            if (data.success == true && !roomId.isNullOrEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra(ChatFragment.ROOM_ID, roomId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "gagal", Toast.LENGTH_LONG).show()
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
        binding = ActivityThankingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object {
        const val EXTRA_PROFILING = "extra_profiling"
    }
}