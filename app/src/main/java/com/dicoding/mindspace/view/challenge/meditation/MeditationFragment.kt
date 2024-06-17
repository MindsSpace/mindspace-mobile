package com.dicoding.mindspace.view.challenge.meditation

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.dicoding.mindspace.R
import com.dicoding.mindspace.databinding.FragmentMeditationBinding

class MeditationFragment : Fragment() {
    private lateinit var binding: FragmentMeditationBinding
    private lateinit var vibrator: Vibrator
    private lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeditationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        handler = Handler()
        setupView()
        setupAction()
    }

    private fun setupAction() {
        binding.startButton.setOnClickListener {
            startMeditation()
        }
        binding.finishBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupView() {
        with(binding) {
            outerCircleView1.apply {
                scaleX = 2f
                scaleY = 2f
            }
            outerCircleView2.apply {
                scaleX = 1.6f
                scaleY = 1.6f
            }
            outerCircleView3.apply {
                scaleX = 1.2f
                scaleY = 1.2f
            }
        }

        binding.finishBtn.isEnabled = false
        binding.startButton.setBackgroundResource(R.drawable.component_button_bg_circle)
        binding.startButton.typeface = ResourcesCompat.getFont(requireContext(), R.font.poppins_semibold)
    }

    private fun startMeditation() {
        val inhaleAnim1 = createScaleAnimation(1f, 2f, binding.outerCircleView1)
        val inhaleAnim2 = createScaleAnimation(1f, 1.6f, binding.outerCircleView2)
        val inhaleAnim3 = createScaleAnimation(1f, 1.2f, binding.outerCircleView3)
        val exhaleAnim1 = createScaleAnimation(2f, 1f, binding.outerCircleView1)
        val exhaleAnim2 = createScaleAnimation(1.6f, 1f, binding.outerCircleView2)
        val exhaleAnim3 = createScaleAnimation(1.2f, 1f, binding.outerCircleView3)

        // Initial duration for inhale and exhale cycles
        val inhaleDuration = 5000L
        val exhaleDuration = 5000L

        binding.finishBtn.isEnabled = false
        binding.instructionText.text = getString(R.string.inhale_instruction)
        binding.outerCircleView1.startAnimation(inhaleAnim1)
        binding.outerCircleView2.startAnimation(inhaleAnim2)
        binding.outerCircleView3.startAnimation(inhaleAnim3)
        startHapticFeedback(inhaleDuration, true)

        // Start countdown timer for inhale cycle
        object : CountDownTimer(inhaleDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update button text with remaining time for inhale cycle
                binding.startButton.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                // Switch to exhale cycle when inhale cycle is finished
                binding.instructionText.text = getString(R.string.exhale_instruction)
                binding.outerCircleView1.startAnimation(exhaleAnim1)
                binding.outerCircleView2.startAnimation(exhaleAnim2)
                binding.outerCircleView3.startAnimation(exhaleAnim3)
                startHapticFeedback(exhaleDuration, false) // Start exhale haptic feedback

                // Start countdown timer for exhale cycle
                object : CountDownTimer(exhaleDuration, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        // Update button text with remaining time for exhale cycle
                        binding.startButton.text = (millisUntilFinished / 1000).toString()
                    }

                    override fun onFinish() {
                        // Meditation is over
                        binding.instructionText.text = getString(R.string.meditate_finish_text)
                        binding.finishBtn.isEnabled = true
                        binding.startButton.text = getString(R.string.retry_text)
                        binding.startButton.isEnabled = true // Enable button after meditation is over
                    }
                }.start()
            }
        }.start()

        // Disable button during meditation
        binding.startButton.isEnabled = false
    }


    private fun createScaleAnimation(from: Float, to: Float, view: View): Animation {
        val scaleAnimation = ScaleAnimation(
            from, to,
            from, to,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        scaleAnimation.duration = 5000L
        scaleAnimation.fillAfter = true
        view.startAnimation(scaleAnimation)
        return scaleAnimation
    }

    private fun startHapticFeedback(duration: Long, inhale: Boolean) {
        val tickIntervals = if (inhale) {
            arrayOf(1500L, 200L, 150L, 100L, 75L, 50L, 50L, 50L, 50L, 75L, 100L, 150L, 200L, 300L, 1500L)
        } else {
            arrayOf(1500L, 150L, 100L, 75L, 50L, 50L, 50L, 50L, 75L, 100L, 150L, 200L, 300L, 1500L)
        }

        var elapsedTime = 0L
        var currentIntervalIndex = 0

        handler.post(object : Runnable {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun run() {
                val interval = tickIntervals[currentIntervalIndex]
                if (interval == 1500L) {
                    // If it's a "stop" phase, do nothing
                } else {
                    vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
                    Log.d("interval and elapsed time", "$interval $elapsedTime")
                    handler.postDelayed({
                        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
                    }, 20)
                }
                if (elapsedTime <= duration) {
                    currentIntervalIndex = (currentIntervalIndex + 1) % tickIntervals.size
                    handler.postDelayed(this, interval)
                    elapsedTime += interval
                }
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}
