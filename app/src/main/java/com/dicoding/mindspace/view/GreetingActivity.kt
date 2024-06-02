package com.dicoding.mindspace.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.mindspace.R
import com.dicoding.mindspace.databinding.ActivityGreetingBinding

class GreetingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGreetingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupAction()
    }

    private fun setupAction() {

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
}