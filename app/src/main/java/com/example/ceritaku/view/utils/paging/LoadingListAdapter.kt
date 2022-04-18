package com.example.ceritaku.view.utils.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ceritaku.databinding.LayoutLoadingBinding

class LoadingListAdapter(private val retry: () -> Unit)
    : LoadStateAdapter<LoadingListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding = LayoutLoadingBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class ViewHolder(val binding : LayoutLoadingBinding,retry: ()-> Unit):
        RecyclerView.ViewHolder(binding.root){
        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState){
            if (loadState is LoadState.Error){
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }
    }


}