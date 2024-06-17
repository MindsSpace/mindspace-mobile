package com.dicoding.mindspace.view.start

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.mindspace.R
import com.dicoding.mindspace.component.CustomCheckBox
import com.dicoding.mindspace.databinding.ActivityProblemBinding

class ProblemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProblemBinding
    private lateinit var optionCheckBoxes: ArrayList<CustomCheckBox>
    private var selectedOptions = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupAction()
    }

    private fun setupAction() {
        optionCheckBoxes = arrayListOf(
            binding.question1Option1,
            binding.question1Option2,
            binding.question1Option3
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

        binding.question1ContinueBtn.setOnClickListener {
            handleProblemSelections()
        }
    }

    private fun handleProblemSelections() {
        val moveHelpIntent = Intent(this, HelpActivity::class.java)
        moveHelpIntent.putStringArrayListExtra(HelpActivity.EXTRA_PROBLEMS, ArrayList(selectedOptions.map { it }))
        moveHelpIntent.putExtra(EXTRA_MOOD, intent.getStringExtra(EXTRA_MOOD))
        startActivity(moveHelpIntent)
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityProblemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object {
        const val EXTRA_MOOD = "mood"
    }
}