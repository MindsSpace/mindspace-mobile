package com.dicoding.mindspace.view.start

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.mindspace.R
import com.dicoding.mindspace.component.CustomCheckBox
import com.dicoding.mindspace.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpBinding
    private lateinit var optionCheckBoxes: ArrayList<CustomCheckBox>
    private var selectedOptions = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupAction()
    }

    private fun setupAction() {
        optionCheckBoxes = arrayListOf(
            binding.question2Option1,
            binding.question2Option2,
            binding.question2Option3
        )

        optionCheckBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    selectedOptions.add(buttonView.text.toString())
                } else {
                    selectedOptions.remove(buttonView.text.toString())
                }
            }
        }

        binding.question2ContinueBtn.setOnClickListener {
            handleHelpSelections()
        }
    }

    private fun handleHelpSelections() {
        val intent = Intent(this, ThankingActivity::class.java)
        startActivity(intent)
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}