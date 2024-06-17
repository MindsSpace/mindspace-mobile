package com.dicoding.mindspace.view.challenge.journal

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dicoding.mindspace.databinding.FragmentNewJournalBinding
import com.dicoding.mindspace.factory.ViewModelWithTokenFactory
import com.dicoding.mindspace.util.getImageUri
import com.dicoding.mindspace.util.reduceFileImage
import com.dicoding.mindspace.util.uriToFile
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NewJournalFragment : Fragment() {
    private lateinit var binding: FragmentNewJournalBinding
    private val viewModel by viewModels<JournalViewModel> {
        ViewModelWithTokenFactory.getInstance(requireContext())
    }
    private var currentImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewJournalBinding.inflate(inflater, container, false)
        setupBackPressHandler()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavTitle()
        setupAction()
        setEdittextFocus()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setShowLoading(isLoading)
        }

        viewModel.journalData.observe(viewLifecycleOwner) { data ->
            if (data.success == true) {
                findNavController().navigateUp()
            }
        }
    }

    private fun setShowLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setNavTitle() {
        val actionBar = (requireActivity() as? AppCompatActivity)?.supportActionBar
        actionBar?.title = getFormattedDate()
    }

    private fun getFormattedDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, dd MMM", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun setEdittextFocus() {
        binding.journalText.requestFocus()
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.journalText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setupAction() {
        val scrollView = binding.scrollView
        val journalText = binding.journalText

        binding.scrollView.setOnClickListener {
            journalText.isFocusableInTouchMode = true
            setEdittextFocus()
        }

        journalText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                scrollView.scrollTo(0, journalText.bottom)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.uploadImageBtn.setOnClickListener {
            startGallery()
        }

        binding.takePhotoBtn.setOnClickListener {
            startCamera()
        }

        binding.clearImageBtn.setOnClickListener {
            clearImage()
        }

        binding.createButton.setOnClickListener {
            if (!journalText.text.isNullOrEmpty()) {
                val content = journalText.text.toString()
                if (currentImageUri != null) {
                    val imageFile = uriToFile(currentImageUri!!, requireContext()).reduceFileImage()
                    viewModel.createJournal(content, imageFile)
                } else {
                    viewModel.createJournal(content, null)
                }
            }
        }
    }

    private fun clearImage() {
        currentImageUri = null
        binding.journalImage.setImageURI(null)
        binding.journalImageLayout.visibility = View.GONE
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.journalImageLayout.visibility = View.VISIBLE
            binding.journalImage.setImageURI(it)
        }
    }

    private fun setupBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
    }
}