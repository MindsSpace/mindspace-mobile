package com.dicoding.mindspace.view.challenge.journal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mindspace.R
import com.dicoding.mindspace.adapter.JournalAdapter
import com.dicoding.mindspace.databinding.FragmentJournalBinding
import com.dicoding.mindspace.factory.ViewModelWithTokenFactory
import com.dicoding.mindspace.factory.ViewModelWithoutTokenFactory
import com.dicoding.mindspace.view.MainActivity
import com.dicoding.mindspace.view.chat.ChatFragment

class JournalFragment : Fragment() {
    private lateinit var binding: FragmentJournalBinding
    private lateinit var journalAdapter: JournalAdapter
    private lateinit var addButton: AppCompatImageButton
    private val viewModel by viewModels<JournalViewModel> {
        ViewModelWithTokenFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJournalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAddJournalMenu()
        setupAdapter()
        observeViewModel()
        viewModel.getAllJournals()
    }

    private fun setupAddJournalMenu() {
        val mainActivity = activity as MainActivity
        addButton = mainActivity.getAddJournalButton()

        addButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_journal_to_navigation_new_journal
            )
        }
    }

    private fun setupAdapter() {
        journalAdapter = JournalAdapter { journal ->
            findNavController().navigate(
                R.id.action_navigation_journal_to_navigation_detail_journal,
                Bundle().apply { putString(DetailJournalFragment.EXTRA_JOURNAL_ID, journal.id) }
            )
        }
        with(binding.rvJournals) {
            val manager = LinearLayoutManager(requireContext())
            manager.reverseLayout = true
            manager.stackFromEnd = true
            layoutManager = manager
            adapter = journalAdapter
        }

        journalAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.rvJournals.scrollToPosition(journalAdapter.itemCount - 1)
            }
        })
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setShowLoading(isLoading)
        }

        viewModel.responseMessage.observe(viewLifecycleOwner) { message ->
            setShowMessage(message)
        }

        viewModel.journals.observe(viewLifecycleOwner) { data ->
            journalAdapter.submitList(data.data)
            binding.rvJournals.scrollToPosition(journalAdapter.itemCount - 1)
        }
    }

    private fun setShowLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setShowMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}