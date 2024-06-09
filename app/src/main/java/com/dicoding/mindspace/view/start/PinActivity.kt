package com.dicoding.mindspace.view.start

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.mindspace.R
import com.dicoding.mindspace.databinding.ActivityPinBinding

class PinActivity : AppCompatActivity(), TextWatcher {
    private lateinit var binding: ActivityPinBinding
    private lateinit var numTemp: String

    private val editTextArray: ArrayList<EditText> = ArrayList(NUM_OF_DIGITS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupEditTexts()
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
                    return
                }
                if (s.length >= 2) {
                    val newTemp = s.toString().substring(s.length - 1)
                    if (newTemp != numTemp) {
                        editTextArray[i].setText(newTemp)
                    } else {
                        editTextArray[i].setText(s.toString().substring(0, s.length - 1))
                    }
                } else if (i != editTextArray.size - 1) { // 1 char
                    editTextArray[i + 1].requestFocus()
                    editTextArray[i + 1].setSelection(editTextArray[i + 1].length())
                    return
                } else {
                    // Test code validity when the last character is inserted
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
        if (verificationCode.isNotEmpty()) {
            // Check code
            enableCodeEditTexts(false)
            val intent = Intent(this, GreetingActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        const val NUM_OF_DIGITS = 4
        const val NICKNAME_EXTRA = "nickname"
    }
}