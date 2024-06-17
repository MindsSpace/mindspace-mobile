package com.dicoding.mindspace.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mindspace.BuildConfig
import com.dicoding.mindspace.data.remote.api.ApiConfig
import com.dicoding.mindspace.data.remote.schema.Journal
import com.dicoding.mindspace.databinding.ItemJournalCardBinding
import com.dicoding.mindspace.util.formatDate
import java.text.SimpleDateFormat
import java.util.Locale

class JournalAdapter(
    private val onClick: (Journal) -> Unit
) : ListAdapter<Journal, JournalAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemJournalCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val journal = getItem(position)
        return holder.bind(journal)
    }

    inner class ViewHolder(private val binding: ItemJournalCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(journal: Journal) {
            binding.apply {
                journalImageLayout.visibility = View.GONE
                if (!journal.image.isNullOrEmpty()) {
                    journalImageLayout.visibility = View.VISIBLE
                    Glide.with(itemView.context)
                        .load(BuildConfig.BASE_URL + "files/" + journal.image)
                        .into(journalImage)
                }
                journalText.text = journal.content
                journalDate.text = formatDate(journal.created_at!!)
                binding.root.setOnClickListener {
                    onClick(journal)
                }
            }
            ViewCompat.setElevation(binding.journalCard, 6f)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Journal>() {
            override fun areItemsTheSame(oldItem: Journal, newItem: Journal): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Journal, newItem: Journal): Boolean {
                return oldItem == newItem
            }
        }
    }
}