package com.mobile.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile.storyapp.databinding.ItemRowStoryBinding
import com.mobile.storyapp.response.ListStory

class ListAdapter : PagingDataAdapter<ListStory, ListAdapter.MyViewHolder>(DIFF_CALLBACK){
    private lateinit var onItemClickCallback: ListAdapter.OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: ListAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
/*        val binding = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_story, parent, false)*/
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
            holder.itemView.setOnClickListener{
                onItemClickCallback.onItemClicked(data)
            }
        }
    }

    class MyViewHolder(private val binding: ItemRowStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
/*        val img: ImageView = itemView.findViewById(R.id.img_item_avatar)
        val username: TextView = itemView.findViewById(R.id.tv_username)

        lateinit var getTask: ListStory

        fun bind(data: ListStory) {
            getTask = data
            Glide.with(itemView.context)
                .load(data.photoUrl)
                .into(img)
            username.text = data.name
            }*/

        fun bind(data: ListStory) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(imgItemAvatar)
                tvUsername.text = data.name
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStory)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStory>() {
            override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
