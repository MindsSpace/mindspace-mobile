package com.dicoding.mindspace.adapter

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mindspace.R
import com.dicoding.mindspace.data.remote.schema.Chat
import com.dicoding.mindspace.databinding.ItemChatBubbleBinding

class ChatAdapter: ListAdapter<Chat, ChatAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = getItem(position)
        if (chat != null) {
            holder.bind(chat)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatBubbleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    inner class ViewHolder(private val binding: ItemChatBubbleBinding, private val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.apply {
                tvChat.text = chat.content
                setChatStyling(chat)
            }
        }

        private fun setChatStyling(chat: Chat) {
            binding.tvChat.visibility = View.VISIBLE
            binding.loadingIndicator.visibility = View.GONE

            if (chat.is_user == true) {
                binding.chatBubble.setBackgroundResource(R.drawable.chat_bubble_bg)
                binding.chatLayout.gravity = Gravity.END
                binding.tvChat.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                binding.chatBubble.setBackgroundResource(R.drawable.chat_bubble_bg_bot)
                binding.chatLayout.gravity = Gravity.START
                binding.tvChat.setTextColor(ContextCompat.getColor(context, R.color.black)) // Assuming bot text color is black

                if (chat.isLoading) {
                    binding.tvChat.visibility = View.GONE
                    binding.loadingIndicator.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Chat>() {
            override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem == newItem
            }
        }
    }
}