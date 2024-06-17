package com.dicoding.mindspace.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mindspace.R
import com.dicoding.mindspace.adapter.HistoryAdapter
import com.dicoding.mindspace.databinding.FragmentHistoryBinding
import com.dicoding.mindspace.factory.ViewModelWithTokenFactory
import com.dicoding.mindspace.factory.ViewModelWithoutTokenFactory
import com.dicoding.mindspace.view.chat.ChatFragment

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelWithTokenFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeViewModel()
        viewModel.getAllRooms()
    }

    private fun setupAdapter() {
        historyAdapter = HistoryAdapter { room ->
            findNavController().navigate(
                R.id.action_global_navigation_chat,
                Bundle().apply { putString(ChatFragment.ROOM_ID, room.id) }
            )
        }

        with(binding.rvHistories) {
            val manager = LinearLayoutManager(requireContext())
            manager.reverseLayout = true
            layoutManager = manager
            adapter = historyAdapter
        }

        historyAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.rvHistories.scrollToPosition(historyAdapter.itemCount - 1)
            }
        })
    }

    private fun observeViewModel() {
        viewModel.responseMessage.observe(viewLifecycleOwner) { message ->
            setShowMessage(message)
        }

        viewModel.historyData.observe(viewLifecycleOwner) { data ->
            historyAdapter.submitList(data.data)
            binding.rvHistories.scrollToPosition(historyAdapter.itemCount - 1)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setShowLoading(isLoading)
        }
    }

    private fun setShowLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setShowMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}