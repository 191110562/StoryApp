package com.mobile.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile.storyapp.databinding.ItemRowStoryBinding
import com.mobile.storyapp.response.ListStory

class StoryAdapter(private val listStory: ArrayList<ListStory>): RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder){
            with(listStory[position]){
                Glide.with(binding.imgItemAvatar)
                    .load(this.photoUrl)
                    .circleCrop()
                    .into(binding.imgItemAvatar)
                binding.tvUsername.text = this.name
                holder.itemView.setOnClickListener{
                    onItemClickCallback.onItemClicked(listStory[holder.adapterPosition])
                }
            }
        }
    }

    override fun getItemCount(): Int = listStory.size

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStory)
    }

    class ListViewHolder(var binding: ItemRowStoryBinding): RecyclerView.ViewHolder(binding.root) {
    }
}