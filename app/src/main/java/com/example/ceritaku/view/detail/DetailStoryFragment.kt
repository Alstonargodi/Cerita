package com.example.ceritaku.view.detail

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.ceritaku.R
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.databinding.FragmentDetailStoryBinding
import com.example.ceritaku.view.home.HomeFragment
import com.example.ceritaku.view.home.HomeFragment.Companion.extra_key_detail
import com.example.ceritaku.view.utils.IdlingConfig
import com.example.ceritaku.view.utils.Utils.dateFormat
import com.example.ceritaku.view.utils.wrapperIdling
import java.lang.Exception


class DetailStoryFragment : Fragment() {
    private lateinit var binding: FragmentDetailStoryBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailStoryBinding.inflate(layoutInflater)

        val data = arguments?.getParcelable<Story>( extra_key_detail)

        try {
            wrapperIdling {
                binding.apply {
                   wrapperIdling {
                       tvdetailstoryDate.text = dateFormat(data?.createdAt.toString())
                       tvdetailstoryDesc.text = data?.description
                       val author = data?.name +  getString(R.string.Detail_title)
                       tvdetailstoryOp.text = author

                       wrapperIdling {
                           Glide.with(binding.root.context)
                               .load(data?.photoUrl)
                               .into(tvdetailstoryImg)
                       }

                   }
                }
            }

        }catch (e : Exception){
            IdlingConfig.decrement()
            emptyData(getString(R.string.Home_empty))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvdetailstoryBack.setOnClickListener {
            val supFragment = requireActivity().supportFragmentManager
            supFragment.beginTransaction()
                .replace(R.id.fragmentviemain,HomeFragment())
                .addToBackStack(null)
                .commit()
        }

    }

    private fun emptyData(message : String){
        binding.detailEmpty.apply {
            root.visibility = View.VISIBLE
            tverror.text = message
            imgerror.setImageResource(R.drawable.ic_notfound)
        }
    }


}