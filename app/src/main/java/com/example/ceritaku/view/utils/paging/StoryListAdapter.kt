package com.example.ceritaku.view.utils.paging

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.DifferCallback
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.data.remote.response.story.StoryResponse
import com.example.ceritaku.databinding.ItemStoryBinding
import com.example.ceritaku.view.home.adapter.StoriesRevHomeAdapter
import com.example.ceritaku.view.utils.Utils


class StoryListAdapter
    : PagingDataAdapter<Story,StoryListAdapter.ViewHolder>(DIFF_CALLBACK)
{


    @RequiresApi(Build.VERSION_CODES.O)
    class ViewHolder(val binding : ItemStoryBinding)
        : RecyclerView.ViewHolder(binding.root){
            fun bind(data : Story){
                val item = data
                binding.apply {
                    val name = "u / ${item.name}"
                    tvStoriesNameop.text = name
                    tvStoriesDate.text = Utils.dateFormat(item.createdAt)
                    tvStoriesDesc.text = item.description

                    Glide.with(root.context)
                        .load(item.photoUrl)
                        .into(tvStoriesImg)

                    tvStoriesDesc.setOnClickListener {
                        tvStoriesDesc.height = 200
                    }
                }

            }
        }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }

    }




    companion object{
        val DIFF_CALLBACK = object :DiffUtil.ItemCallback<Story>(){
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Story,
                newItem: Story
            ): Boolean {
                return oldItem == newItem
            }

        }
    }


}