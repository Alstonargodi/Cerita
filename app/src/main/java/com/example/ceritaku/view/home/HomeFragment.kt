package com.example.ceritaku.view.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ceritaku.databinding.FragmentHomeBinding
import com.example.ceritaku.data.remote.utils.Result
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.view.home.adapter.StoriesRevHomeAdapter
import com.example.ceritaku.viewmodel.StoryViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel : StoryViewModel by viewModels{
        VModelFactory.getInstance()
    }
    private lateinit var recviewAdapter : StoriesRevHomeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            getStoryList()
        }
        return binding.root
    }

    private suspend fun getStoryList(){
        val page = 0
        val size = 100

        viewModel.getStoryList(page, size, authKey).observe(viewLifecycleOwner){
            when(it){
                is Result.Loading->{
                    binding.pgbarhome.visibility = View.VISIBLE
                }
                is Result.Sucess->{
                    binding.pgbarhome.visibility = View.GONE
                    showRecyclerList(it.data.listStory)

                }
                is Result.Error->{
                    binding.pgbarhome.visibility = View.GONE
                    Log.d("getStorylist",it.error)
                }
            }
        }
    }

    private fun showRecyclerList(data : List<Story>){
        recviewAdapter = StoriesRevHomeAdapter(data)
        binding.listStoryHome.adapter = recviewAdapter
        binding.listStoryHome.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object{
        const val authKey = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWp6b2pEWUd4NFV0cUs5clUiLCJpYXQiOjE2NDkzOTc3OTh9.VrMbbMLriptuq8rmNfBGA2VZ88CNVJ6hJm93IAdcg7k"
    }


}