package com.dicoding.mindspace.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.mindspace.R
import com.dicoding.mindspace.databinding.ActivityMoodProfilingBinding

class MoodProfilingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoodProfilingBinding
    private var initialY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupAction()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupAction() {
        val swipeArea = binding.swipeArea
        val emoji = binding.emojiView

        swipeArea.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialY = event.y
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaY = initialY - event.y
                    emoji.updateEmotionLevel(deltaY.toInt())
                    initialY = event.y
                    true
                }
                else -> false
            }
        }

        val emotionLevel = emoji.getEmotionLevel()
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityMoodProfilingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}