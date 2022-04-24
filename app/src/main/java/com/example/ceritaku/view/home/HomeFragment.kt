package com.example.ceritaku.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ceritaku.R
import com.example.ceritaku.databinding.FragmentHomeBinding
import com.example.ceritaku.view.home.adapter.SectionPagerAdapter
import com.example.ceritaku.view.utils.IdlingConfig
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var pagerAdapter : SectionPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentHomeBinding.inflate(layoutInflater)

        pagerAdapter = SectionPagerAdapter(requireActivity(), tab_titles.size)
        val viewPager = binding.viewPagerHome
        val tabs = binding.tabLayoutHome
        viewPager.adapter = pagerAdapter
        viewPager.isUserInputEnabled = false
        TabLayoutMediator(tabs,viewPager){tab,position->
            tab.text = getString(tab_titles[position])
            IdlingConfig.decrement()
        }.attach()

        return binding.root
    }





    companion object{
        const val TAG = "HomeFragment"

        const val extra_key_detail = "detailstory"

        var tab_titles = intArrayOf(
            R.string.tabone,
            R.string.tabtwo
        )
    }




}