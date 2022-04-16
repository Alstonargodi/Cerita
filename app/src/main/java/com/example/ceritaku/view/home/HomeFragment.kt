package com.example.ceritaku.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ceritaku.R
import com.example.ceritaku.data.local.UserPrefrences
import com.example.ceritaku.data.local.dataStore
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.data.remote.utils.Result
import com.example.ceritaku.databinding.FragmentHomeBinding
import com.example.ceritaku.databinding.LayoutBoard1Binding
import com.example.ceritaku.view.detail.DetailStoryFragment
import com.example.ceritaku.view.home.adapter.StoriesRevHomeAdapter
import com.example.ceritaku.viewmodel.StoryViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import com.example.ceritaku.viewmodel.utils.PrefViewModelFactory
import com.example.ceritaku.viewmodel.utils.SettingPrefViewModel
import kotlinx.coroutines.launch



class HomeFragment : Fragment() {
    private val viewModel : StoryViewModel by viewModels{ VModelFactory.getInstance() }
    private lateinit var recViewAdapter : StoriesRevHomeAdapter

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bindingError : LayoutBoard1Binding

    private lateinit var prefViewModel : SettingPrefViewModel
    private var userToken = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
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


    private suspend fun getStoryList(userTokene : String){
        viewModel.getStoryList(page, size, userTokene).observe(viewLifecycleOwner){
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
                    fetchError(it.error + getString(R.string.Home_error))
                    Log.d(TAG,it.error)
                }
            }
        }
    }

    private fun showRecyclerList(data : List<Story>){
        recViewAdapter = StoriesRevHomeAdapter(data)
        binding.listStoryHome.adapter = recViewAdapter
        binding.listStoryHome.layoutManager = LinearLayoutManager(requireContext())
        viewModel.setEmptys(false)

        if (recViewAdapter.itemCount == 0){
            emptyData(getString(R.string.Home_empty))
        }
        recViewAdapter.onItemClickDetail(object : StoriesRevHomeAdapter.OnClickDetail{
            override fun onItemClickDetail(data: Story) {

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