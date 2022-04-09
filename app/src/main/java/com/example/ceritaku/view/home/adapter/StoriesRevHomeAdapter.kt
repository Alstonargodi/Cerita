package com.example.ceritaku.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ceritaku.databinding.ItemStoryBinding
import com.example.ceritaku.data.remote.response.story.Story

class StoriesRevHomeAdapter(private val dataList : List<Story>):
    RecyclerView.Adapter<StoriesRevHomeAdapter.ViewHolder>()
    {
        class ViewHolder(val binding : ItemStoryBinding):RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ItemStoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = dataList[position]
            holder.binding.apply {
                tvStoriesNameop.text = item.name
                tvStoriesDate.text = item.createdAt
                tvStoriesDesc.text = item.description

                Glide.with(root.context)
                    .load(item.photoUrl)
                    .into(tvStoriesImg)

                tvStoriesDesc.setOnClickListener {
                    tvStoriesDesc.height = 200
                }

            }
        }

        override fun getItemCount(): Int = dataList.size

    }