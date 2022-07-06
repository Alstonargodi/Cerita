package com.example.ceritaku.view.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ceritaku.view.home.liststory.ListStoryFragment
import com.example.ceritaku.view.home.maps.MapsFragment

class SectionPagerAdapter(activity : FragmentActivity, private var tab : Int): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = tab

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ListStoryFragment()
            1 -> MapsFragment()
            else -> Fragment()
        }
    }

}