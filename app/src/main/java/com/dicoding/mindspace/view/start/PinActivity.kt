package com.dicoding.mindspace.view.start

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import com.dicoding.mindspace.R
import com.dicoding.mindspace.data.remote.schema.RegisterRequest
import com.dicoding.mindspace.databinding.ActivityPinBinding
import com.dicoding.mindspace.factory.ViewModelWithoutTokenFactory
import com.google.android.material.snackbar.Snackbar

class PinActivity : AppCompatActivity(), TextWatcher {
    private lateinit var binding: ActivityPinBinding
    private lateinit var numTemp: String
    private lateinit var pin: String
    private lateinit var greetingActivityResult: ActivityResultLauncher<Intent>

    private val editTextArray: ArrayList<EditText> = ArrayList(NUM_OF_DIGITS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupEditTexts()
        setupAction()
    }

    private fun setupAction() {
        binding.snackBtn.setOnClickListener {
            val nickname = intent.getStringExtra(NICKNAME_EXTRA).toString()

            val intent = Intent(this, GreetingActivity::class.java)
            intent.putExtra(GreetingActivity.EXTRA_NICKNAME, nickname)
            intent.putExtra(GreetingActivity.EXTRA_PIN, pin)
            startActivity(intent)
        }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityPinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.pinDescription.text = resources.getString(
            R.string.pin_description, intent.getStringExtra(
                NICKNAME_EXTRA
            )
        )
        binding.snackBtn.isEnabled = false
    }

    private fun setupEditTexts() {
        // Add EditText views to the array
        editTextArray.apply {
            add(binding.digit1)
            add(binding.digit2)
            add(binding.digit3)
            add(binding.digit4)
        }

        // Set up text change and key listeners
        for (index in 0 until editTextArray.size) {
            editTextArray[index].addTextChangedListener(this)
            editTextArray[index].setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    if (index != 0) {
                        editTextArray[index - 1].requestFocus()
                        editTextArray[index - 1].setSelection(editTextArray[index - 1].length())
                    }
                }
                false
            }
        }

        editTextArray[0].requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextArray[0], InputMethodManager.SHOW_IMPLICIT)
    }

    private fun enableCodeEditTexts(enable: Boolean) {
        for (i in editTextArray.indices)
            editTextArray[i].isEnabled = enable
    }

    private fun initCodeEditTexts() {
        for (i in editTextArray.indices)
            editTextArray[i].setText("")

        editTextArray[0].requestFocus()
    }

    // Code auto set
    private fun setVerificationCode(verificationCode: String) {
        for (i in editTextArray.indices)
            editTextArray[i].setText(verificationCode.substring(i, i + 1))
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        numTemp = s.toString()
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        for (i in editTextArray.indices) {
            if (s === editTextArray[i].editableText) {
                if (s.isBlank()) {
                    binding.snackBtn.isEnabled = false
                    return
                }
                if (s.length >= 2) {
                    val newTemp = s.toString().substring(s.length - 1)
                    binding.snackBtn.isEnabled = false
                    if (newTemp != numTemp) {
                        editTextArray[i].setText(newTemp)
                    } else {
                        editTextArray[i].setText(s.toString().substring(0, s.length - 1))
                    }
                } else if (i != editTextArray.size - 1) { // 1 char
                    editTextArray[i + 1].requestFocus()
                    editTextArray[i + 1].setSelection(editTextArray[i + 1].length())
                    binding.snackBtn.isEnabled = false
                    return
                } else {
                    // Test code validity when the last character is inserted
                    binding.snackBtn.isEnabled = true
                    verifyCode(testCodeValidity())
                }
            }
        }
    }

    private fun testCodeValidity(): String {
        var verificationCode = ""
        for (j in editTextArray.indices) {
            val digit = editTextArray[j].text.toString().trim()
            verificationCode += digit
        }
        return if (verificationCode.length == NUM_OF_DIGITS) {
            verificationCode
        } else {
            ""
        }
    }

    private fun verifyCode(verificationCode: String) {
        if (verificationCode.isNotEmpty() && verificationCode.length == 4) {
            // Check code
            // enableCodeEditTexts(false)
            pin = verificationCode
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.main.windowToken, 0)
            binding.snackBtn.requestFocus()
        }
    }

    companion object {
        const val NUM_OF_DIGITS = 4
        const val NICKNAME_EXTRA = "nickname"
    }
}