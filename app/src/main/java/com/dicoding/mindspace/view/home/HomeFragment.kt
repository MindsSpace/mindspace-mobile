package com.dicoding.mindspace.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dicoding.mindspace.R
import com.dicoding.mindspace.data.pref.UserPreference
import com.dicoding.mindspace.data.pref.dataStore
import com.dicoding.mindspace.data.remote.schema.DailyStreakData
import com.dicoding.mindspace.databinding.FragmentHomeBinding
import com.dicoding.mindspace.factory.ViewModelWithTokenFactory
import com.dicoding.mindspace.view.start.MoodProfilingActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dayContainer: GridLayout
    private lateinit var dayInfoContainer: FrameLayout
    private lateinit var userPreference: UserPreference

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelWithTokenFactory.getInstance(requireContext())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userPreference = UserPreference.getInstance(requireContext().dataStore)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setNavTitle()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setElevation(binding.card1, 4f)
        ViewCompat.setElevation(binding.card2, 4f)
        observeViewModel()
        setupAction()
        viewModel.getWeeklyStreak()
    }

    private fun setNavTitle() {
        viewLifecycleOwner.lifecycleScope.launch {
            userPreference.getSession().collect { user ->
                val actionBar = (activity as? AppCompatActivity)?.supportActionBar
                actionBar?.title = getString(R.string.user_greet, user.username)
            }
        }
    }

    private fun setupAction() {
        binding.card1.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_home_to_navigation_meditation
            )
        }
        binding.card2.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_home_to_navigation_new_journal
            )
        }
    }

    private fun observeViewModel() {
        viewModel.weeklyStreakData.observe(viewLifecycleOwner) { streakData ->
            val data = streakData.data
            if (data != null) {
                if (data[data.lastIndex].is_filled == false) {
                    startActivity(Intent(requireActivity(), MoodProfilingActivity::class.java))
                } else {
                    setupMoodTracker(data)
                }
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { _ -> }
    }

    private fun setupMoodTracker(weeklyStreakData: List<DailyStreakData>) {
        dayContainer = binding.dayContainer
        dayInfoContainer = binding.dayInfoContainer

        // Clear existing views
        dayContainer.removeAllViews()

        for (streakData in weeklyStreakData) {
            val date = parseDate(streakData.datetime)
            addOrUpdateDayButton(date, streakData)
        }

        // Select the current day
        val today = SimpleDateFormat("MMM dd", Locale.getDefault()).format(Calendar.getInstance().time)
        val todayIndex = weeklyStreakData.indexOfFirst {
            SimpleDateFormat("MMM dd", Locale.getDefault()).format(parseDate(it.datetime)) == today
        }
        if (todayIndex != -1) {
            updateDayInfo(weeklyStreakData[todayIndex])
            setSelectedDayButton(dayContainer.getChildAt(todayIndex))
        }
    }

    private fun parseDate(datetime: String?): Date {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS ZZZZZ", Locale.getDefault())
        return datetime?.let { dateFormat.parse(it) } ?: Date()
    }

    private fun addOrUpdateDayButton(date: Date, streakData: DailyStreakData) {
        // Inflate a new day button or update the existing one
        val dayButton = layoutInflater.inflate(R.layout.day_button, dayContainer, false)
        val tvMonth = dayButton.findViewById<TextView>(R.id.tvMonth)
        val tvDate = dayButton.findViewById<TextView>(R.id.tvDate)
        val circleMark = dayButton.findViewById<View>(R.id.circleMark)

        val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())

        tvMonth.text = monthFormat.format(date)
        tvDate.text = dateFormat.format(date)

        circleMark.isSelected = streakData.is_filled == true

        dayButton.setOnClickListener {
            updateDayInfo(streakData)
            setSelectedDayButton(dayButton)
        }

        dayContainer.addView(dayButton)
    }

    private fun setSelectedDayButton(selectedButton: View) {
        for (i in 0 until dayContainer.childCount) {
            val child = dayContainer.getChildAt(i)
            val tvMonth = child.findViewById<TextView>(R.id.tvMonth)
            val tvDate = child.findViewById<TextView>(R.id.tvDate)

            if (child == selectedButton) {
                child.setBackgroundResource(R.drawable.component_tracker_bg_on)
                tvMonth.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                tvDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

            } else {
                child.setBackgroundResource(R.drawable.component_tracker_bg)
                tvMonth.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                tvDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
            }
        }
    }

    private fun updateDayInfo(streakData: DailyStreakData) {
        // Clear existing info views
        dayInfoContainer.removeAllViews()

        val infoView = layoutInflater.inflate(R.layout.day_info, dayInfoContainer, false)
        val tvTitle = infoView.findViewById<TextView>(R.id.tvTitle)
        val tvDesc = infoView.findViewById<TextView>(R.id.tvDesc)
        tvTitle.text = if (streakData.mood.isNullOrEmpty()) "No Mood Data" else streakData.mood
        tvDesc.text = streakData.problems?.first() ?: "No Advice Available"

        dayInfoContainer.addView(infoView)
    }
}