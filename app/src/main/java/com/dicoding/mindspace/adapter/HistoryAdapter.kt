package com.dicoding.mindspace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mindspace.data.remote.schema.Room
import com.dicoding.mindspace.databinding.ItemHistoryCardBinding
import com.dicoding.mindspace.util.formatDate
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(private val onClick: (Room) -> Unit) :
    ListAdapter<Room, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHistoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = getItem(position)
        return holder.bind(history)
    }

    inner class ViewHolder(private val binding: ItemHistoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(history: Room) {
            binding.apply {
                tvName.text = history.name
                tvTimestamp.text = formatDate(history.created_at ?: "")
                root.setOnClickListener {
                    onClick(history)
                }
            }

            ViewCompat.setElevation(binding.historyCard, 6f)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Room>() {
            override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
                return oldItem == newItem
            }
        }
    }
}