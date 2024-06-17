package com.dicoding.mindspace.view.start

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.mindspace.R
import com.dicoding.mindspace.data.pref.UserPreference
import com.dicoding.mindspace.data.pref.dataStore
import com.dicoding.mindspace.databinding.ActivityMoodProfilingBinding
import kotlinx.coroutines.launch

class MoodProfilingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoodProfilingBinding
    private lateinit var userPreference: UserPreference
    private var initialY = 0f
    private var mood: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        userPreference = UserPreference.getInstance(this.dataStore)
        setUsername()
        setupAction()
    }

    private fun setUsername() {
        lifecycleScope.launch {
            userPreference.getSession().collect { user ->
                binding.tvMoodQuestion.text = resources.getString(R.string.mood_profiling_question, user.username)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupAction() {
        val swipeArea = binding.swipeArea
        val emoji = binding.emojiView
        val continueBtn = binding.moodContinueBtn

        continueBtn.setOnClickListener {
            val intent = Intent(this, ProblemActivity::class.java)
            intent.putExtra(ProblemActivity.EXTRA_MOOD, mood)
            startActivity(intent)
        }

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

        emoji.emotionLevelLiveData.observe(this) { emotionLevel ->
            binding.moodContinueBtn.text = emotionLevel
            mood = emotionLevel
        }
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