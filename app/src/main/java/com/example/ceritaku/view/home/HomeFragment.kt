package com.example.ceritaku.view.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ceritaku.data.local.UserPrefrencesConfig
import com.example.ceritaku.data.local.entity.UserDetailModel
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
    private lateinit var recViewAdapter : StoriesRevHomeAdapter
    private var userDetailModel: UserDetailModel = UserDetailModel()
    private lateinit var userPrefConfig: UserPrefrencesConfig
    private var userToken = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        userPrefConfig = UserPrefrencesConfig(requireContext())
        userDetailModel = userPrefConfig.getUserDetail()
        userToken = "Bearer ${userDetailModel.token.toString()}"

        lifecycleScope.launch { getStoryList() }

        return binding.root
    }


    private suspend fun getStoryList(){
        viewModel.getStoryList(page, size, userToken).observe(viewLifecycleOwner){
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
                    showError(it.error)
                    Log.d("getStorylist",it.error)
                }
            }
        }
    }

    private fun showRecyclerList(data : List<Story>){
        recViewAdapter = StoriesRevHomeAdapter(data)
        binding.listStoryHome.adapter = recViewAdapter
        binding.listStoryHome.layoutManager = LinearLayoutManager(requireContext())

        recViewAdapter.onItemClickDetail(object : StoriesRevHomeAdapter.OnClickDetail{
            override fun onItemClickDetail(data: Story) {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailStoryFragment(data))
            }
        })


    }

    private fun showError(error : String){
        binding.tverrorview.apply {
            visibility = View.VISIBLE
            text = error + "\n please try again :("
            setOnClickListener {
                lifecycleScope.launch {
                    getStoryList()
                    visibility = View.GONE
                }
            }
        }
    }

    companion object{
        const val page = 0
        const val size = 100
    }




}