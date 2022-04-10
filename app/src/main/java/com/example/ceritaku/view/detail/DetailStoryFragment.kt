package com.example.ceritaku.view.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ceritaku.R
import com.example.ceritaku.databinding.FragmentDetailStoryBinding


class DetailStoryFragment : Fragment() {
    private lateinit var binding: FragmentDetailStoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailStoryBinding.inflate(layoutInflater)


        val data = DetailStoryFragmentArgs.fromBundle(arguments as Bundle).detailStory

        binding.apply {
            tvdetailstoryDate.text = data.createdAt
            tvdetailstoryDesc.text = data.description
            tvdetailstoryOp.text = data.name + " story"


            Glide.with(binding.root.context)
                .load(data.photoUrl)
                .into(tvdetailstoryImg)
        }

        binding.tvdetailstoryBack.setOnClickListener {
            findNavController().navigate(
                DetailStoryFragmentDirections.actionDetailStoryFragmentToHomeFragment()
            )
        }






        return binding.root
    }


}