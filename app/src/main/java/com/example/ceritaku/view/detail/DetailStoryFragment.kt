package com.example.ceritaku.view.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.ceritaku.R
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.databinding.FragmentDetailStoryBinding
import com.example.ceritaku.view.home.HomeFragment
import com.example.ceritaku.view.home.HomeFragment.Companion.extra_key_detail


class DetailStoryFragment : Fragment() {
    private lateinit var binding: FragmentDetailStoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailStoryBinding.inflate(layoutInflater)

        val data = arguments?.getParcelable<Story>(extra_key_detail)

        binding.apply {
            tvdetailstoryDate.text = data?.createdAt
            tvdetailstoryDesc.text = data?.description
            val author = data?.name +  getString(R.string.Detail_title)
            tvdetailstoryOp.text = author

            Glide.with(binding.root.context)
                .load(data?.photoUrl)
                .into(tvdetailstoryImg)
        }

        binding.tvdetailstoryBack.setOnClickListener {
            val supFragment = requireActivity().supportFragmentManager
            supFragment.beginTransaction()
                .replace(R.id.fragmentview,HomeFragment())
                .addToBackStack(null)
                .commit()

        }


        return binding.root
    }


}