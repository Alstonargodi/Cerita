package com.example.ceritaku.view.home.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ceritaku.databinding.ItemStoryBinding
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.view.utils.dateFormat


class StoriesRevHomeAdapter(private val dataList : List<Story>):
    RecyclerView.Adapter<StoriesRevHomeAdapter.ViewHolder>()
    {
        class ViewHolder(val binding : ItemStoryBinding):RecyclerView.ViewHolder(binding.root)

        private lateinit var onItemCLickDetail : OnClickDetail

        fun onItemClickDetail(onItemCLickDetail : OnClickDetail){
            this.onItemCLickDetail = onItemCLickDetail
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ItemStoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = dataList[position]


            holder.binding.apply {
                val name = "u / ${item.name}"
                tvStoriesNameop.text = name
                tvStoriesDate.text = dateFormat(item.createdAt)
                tvStoriesDesc.text = item.description

                Glide.with(root.context)
                    .load(item.photoUrl)
                    .into(tvStoriesImg)

                tvStoriesDesc.setOnClickListener {
                    tvStoriesDesc.height = 200
                }

                tvStoriesImg.setOnClickListener {
                    onItemCLickDetail.onItemClickDetail(item)
                }

            }
        }

        override fun getItemCount(): Int = dataList.size


        interface OnClickDetail{
            fun onItemClickDetail(data : Story)
        }
    }