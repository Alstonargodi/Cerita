package com.example.ceritaku.view.home.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.databinding.ItemStoryBinding
import com.example.ceritaku.view.utils.IdlingConfig
import com.example.ceritaku.view.utils.Utils
import com.example.ceritaku.view.utils.wrapperIdling


class StoryListAdapter
    : PagingDataAdapter<Story, StoryListAdapter.ViewHolder>(DIFF_CALLBACK)
{
    private lateinit var onClickDetail : OnClickDetail

    fun onItemClickDetail(onItemCLickDetail : OnClickDetail){
        this.onClickDetail = onItemCLickDetail
    }


    @RequiresApi(Build.VERSION_CODES.O)
    class ViewHolder(val binding : ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = getItem(position)
            if (data !=null){
                holder.binding.apply {
                    val name = "u / ${data.name}"

                    tvStoriesNameop.text = name
                    tvStoriesDate.text = Utils.dateFormat(data.createdAt)
                    tvStoriesDesc.text = data.description


                    Glide.with(root.context)
                        .load(data.photoUrl)
                        .into(tvStoriesImg)

                    tvStoriesDesc.setOnClickListener {
                        tvStoriesDesc.height = 200
                    }

                    holder.binding.root.setOnClickListener {
                        onClickDetail.onClickDetail(data)
                    }
                }

            }
    }

    interface OnClickDetail{
        fun onClickDetail(data : Story)

    }




    companion object{
        val DIFF_CALLBACK = object :DiffUtil.ItemCallback<Story>(){
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
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