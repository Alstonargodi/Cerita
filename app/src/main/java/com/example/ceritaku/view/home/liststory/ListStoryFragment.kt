package com.example.ceritaku.view.home.liststory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import com.example.ceritaku.view.utils.IdlingConfig
import com.example.ceritaku.view.utils.wrapperIdling
import com.example.ceritaku.viewmodel.StoryViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import com.example.ceritaku.viewmodel.utils.PrefViewModelFactory
import com.example.ceritaku.viewmodel.utils.SettingPrefViewModel
import kotlinx.coroutines.launch


class ListStoryFragment : Fragment() {
    private val viewModel : StoryViewModel by viewModels{ VModelFactory.getInstance(requireActivity()) }
    private lateinit var binding: FragmentListStoryBinding
    private lateinit var bindingError : LayoutBoard1Binding

    private lateinit var prefViewModel : SettingPrefViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListStoryBinding.inflate(layoutInflater)
        bindingError = LayoutBoard1Binding.inflate(layoutInflater)

        prefViewModel = ViewModelProvider(requireActivity(),
            PrefViewModelFactory(
                UserPrefrences.getInstance(requireContext().dataStore)
            )
        )[SettingPrefViewModel::class.java]

            fetchData()

        return binding.root
    }


    private fun fetchData(){
        prefViewModel.getUserToken().observe(viewLifecycleOwner){
            showRecyclerList("Bearer $it")
        }
    }
    private fun showRecyclerList(userToken : String){
        wrapperIdling {
            val rViewAdapter = StoryListAdapter()
            binding.listHomeStory.adapter = rViewAdapter.withLoadStateFooter(
                footer = LoadingListAdapter{
                    rViewAdapter.retry()
                }
            )

            viewModel.getStoryList(userToken).observe(viewLifecycleOwner){
                rViewAdapter.submitData(requireActivity().lifecycle,it)
            }


            binding.listHomeStory.layoutManager = LinearLayoutManager(requireContext())

            rViewAdapter.onItemClickDetail(object : StoryListAdapter.OnClickDetail{
                override fun onClickDetail(data: Story) {
                    val bundle = Bundle()
                    val fragment = DetailStoryFragment()
                    bundle.putParcelable(extra_key_detail,data)
                    fragment.arguments = bundle
                    val supFragment = requireActivity().supportFragmentManager
                    supFragment.beginTransaction()
                        .replace(R.id.fragmentviemain,fragment)
                        .addToBackStack(null)
                        .commit()
                }
            })

        }
    }


    //todo empty error
    private fun emptyData(message : String){
        binding.layoutEmpty.apply {
            root.visibility = View.VISIBLE
            tverror.text = message
            imgerror.setImageResource(R.drawable.ic_notfound)
            imgerror.setOnClickListener {
                lifecycleScope.launch {
                    fetchData()
                    root.visibility = View.GONE
                }
            }
        }
    }


    companion object{
        const val extra_key_detail = "detailstory"
    }

}