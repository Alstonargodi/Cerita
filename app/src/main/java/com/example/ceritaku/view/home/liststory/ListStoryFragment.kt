package com.example.ceritaku.view.home.liststory

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ceritaku.R
import com.example.ceritaku.data.local.UserPrefrences
import com.example.ceritaku.data.local.dataStore
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.databinding.FragmentListStoryBinding
import com.example.ceritaku.databinding.LayoutBoard1Binding
import com.example.ceritaku.view.utils.paging.LoadingListAdapter
import com.example.ceritaku.view.utils.paging.StoryListAdapter
import com.example.ceritaku.viewmodel.StoryViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import com.example.ceritaku.viewmodel.utils.PrefViewModelFactory
import com.example.ceritaku.viewmodel.utils.SettingPrefViewModel
import kotlinx.coroutines.launch


class ListStoryFragment : Fragment() {
    private val viewModel : StoryViewModel by viewModels{ VModelFactory.getInstance() }
    private lateinit var binding: FragmentListStoryBinding
    private lateinit var bindingError : LayoutBoard1Binding

    private lateinit var prefViewModel : SettingPrefViewModel
    private var userToken = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListStoryBinding.inflate(layoutInflater)
        bindingError = LayoutBoard1Binding.inflate(layoutInflater)

        prefViewModel = ViewModelProvider(requireActivity(),
            PrefViewModelFactory(
                UserPrefrences.getInstance(requireContext().dataStore)
            )
        )[SettingPrefViewModel::class.java]

        prefViewModel.getUserToken().observe(viewLifecycleOwner){
            lifecycleScope.launch {
                getStoryList("Bearer $it")
                userToken = "Bearer $it"
            }
        }

        return binding.root
    }

    private fun getStoryList(userToken : String){
        viewModel.getStoryList(userToken).observe(viewLifecycleOwner){
            showRecyclerList(it)
        }
    }

    private  fun showRecyclerList(data : PagingData<Story>){
        val rViewAdapter = StoryListAdapter()
        binding.listHomeStory.adapter = rViewAdapter.withLoadStateFooter(
            footer = LoadingListAdapter{
                rViewAdapter.retry()
            }
        )
        rViewAdapter.submitData(lifecycle,data)

        binding.listHomeStory.layoutManager = LinearLayoutManager(requireContext())


    }

    private fun fetchError(message : String){
        binding.layoutEmpty.apply {
            root.visibility = View.VISIBLE
            tverror.text = message
            imgerror.setImageResource(R.drawable.ic_error_connect)
            imgerror.setOnClickListener {
                lifecycleScope.launch {
                    getStoryList(userToken)
                    root.visibility = View.GONE
                }
            }
        }
    }

    private fun emptyData(message : String){
        binding.layoutEmpty.apply {
            root.visibility = View.VISIBLE
            tverror.text = message
            imgerror.setImageResource(R.drawable.ic_notfound)
            imgerror.setOnClickListener {
                lifecycleScope.launch {
                    getStoryList(userToken)
                    root.visibility = View.GONE
                }
            }
        }
    }



    companion object{
        const val TAG = "HomeFragment"
        const val page = 0
        const val size = 100
        const val extra_key_detail = "detailstory"
    }

}