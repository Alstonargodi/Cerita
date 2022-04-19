package com.example.ceritaku.view.home.liststory

import android.os.Bundle
import android.util.Log

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
import com.example.ceritaku.data.local.datastore.UserPrefrences
import com.example.ceritaku.data.local.datastore.dataStore
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.databinding.FragmentListStoryBinding
import com.example.ceritaku.databinding.LayoutBoard1Binding
import com.example.ceritaku.view.detail.DetailStoryFragment
import com.example.ceritaku.view.utils.paging.LoadingListAdapter
import com.example.ceritaku.view.home.adapter.StoryListAdapter
import com.example.ceritaku.view.home.maps.MapsFragment
import com.example.ceritaku.viewmodel.StoryViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import com.example.ceritaku.viewmodel.utils.PrefViewModelFactory
import com.example.ceritaku.viewmodel.utils.SettingPrefViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ListStoryFragment : Fragment() {
    private val viewModel : StoryViewModel by viewModels{ VModelFactory.getInstance(requireActivity()) }
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
                showRecyclerList("Bearer $it")
                userToken = "Bearer $it"
            }
        }
        return binding.root
    }



    private suspend fun showRecyclerList(userToken : String){
        val rViewAdapter = StoryListAdapter()
        binding.listHomeStory.adapter = rViewAdapter.withLoadStateFooter(
            footer = LoadingListAdapter{
                rViewAdapter.retry()
            }
        )
        viewModel.getStoryList(userToken).observe(viewLifecycleOwner){
            rViewAdapter.submitData(requireActivity().lifecycle,it)
        }
//        viewModel.getStoryList(userToken).collect{
//            rViewAdapter.submitData(lifecycle,it)
//            Log.d("data",it.toString())
//        }


        binding.listHomeStory.layoutManager = LinearLayoutManager(requireContext())

        rViewAdapter.onItemClickDetail(object : StoryListAdapter.OnClickDetail{
            override fun onClickDetail(data: Story) {
                val bundle = Bundle()
                val fragment = DetailStoryFragment()
                bundle.putParcelable(extra_key_detail,data)
                fragment.arguments = bundle
                val supFragment = requireActivity().supportFragmentManager
                supFragment.beginTransaction()
                    .replace(R.id.fragmentview,fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })


    }




    companion object{
        const val TAG = "HomeFragment"
        const val page = 0
        const val size = 100
        const val extra_key_detail = "detailstory"
    }

}