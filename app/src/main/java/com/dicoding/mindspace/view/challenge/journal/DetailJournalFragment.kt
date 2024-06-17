package com.dicoding.mindspace.view.challenge.journal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dicoding.mindspace.BuildConfig
import com.dicoding.mindspace.R
import com.dicoding.mindspace.data.remote.api.GetJournalByIdResponse
import com.dicoding.mindspace.databinding.FragmentDetailJournalBinding
import com.dicoding.mindspace.factory.ViewModelWithTokenFactory
import com.dicoding.mindspace.util.formatDate
import com.dicoding.mindspace.view.MainActivity
import kotlinx.coroutines.launch

class DetailJournalFragment : Fragment() {
    private lateinit var binding: FragmentDetailJournalBinding
    private lateinit var deleteButton: AppCompatImageButton
    private val viewModel by viewModels<JournalViewModel> {
        ViewModelWithTokenFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailJournalBinding.inflate(inflater, container, false)
        setupBackPressHandler()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDeleteJournalMenu()
        observeViewModel()
        viewModel.getJournalById(arguments?.getString(EXTRA_JOURNAL_ID) ?: "")
    }

    private fun setupDeleteJournalMenu() {
        val mainActivity = activity as MainActivity
        deleteButton = mainActivity.getDeleteJournalButton()

        deleteButton.setOnClickListener {
            viewModel.deleteJournal(arguments?.getString(EXTRA_JOURNAL_ID) ?: "")
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setShowLoading(isLoading)
        }

        viewModel.journal.observe(viewLifecycleOwner) { data ->
            updateView(data)
        }

        viewModel.isLoadingDelete.observe(viewLifecycleOwner) { isLoadingDelete ->
            setShowLoadingDelete(isLoadingDelete)
        }

        viewModel.deleteData.observe(viewLifecycleOwner) { response ->
            if (response.success == true) {
                findNavController().navigateUp()
            }
        }
    }

    private fun updateView(data: GetJournalByIdResponse) {
        binding.apply {
            journalText.text = data.data?.content
            journalDate.text = formatDate(data.data?.created_at!!)
            if (data.data.image != null) {
                binding.journalImageLayout.visibility = View.VISIBLE
                Glide.with(requireContext())
                    .load(BuildConfig.BASE_URL + "files/" + data.data.image)
                    .into(journalImage)
            }
        }
    }

    private fun setShowLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setShowLoadingDelete(isLoading: Boolean) {
        binding.progressBarLinear.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
    }

    companion object {
        const val EXTRA_JOURNAL_ID = "extra_journal_id"
    }
}