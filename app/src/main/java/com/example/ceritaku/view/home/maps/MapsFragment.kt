package com.example.ceritaku.view.home.maps

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.ceritaku.R
import com.example.ceritaku.data.local.datastore.UserPrefrences
import com.example.ceritaku.data.local.datastore.dataStore
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.data.remote.utils.MediatorResult
import com.example.ceritaku.view.detail.DetailStoryFragment
import com.example.ceritaku.view.home.liststory.ListStoryFragment
import com.example.ceritaku.view.utils.IdlingConfig
import com.example.ceritaku.view.utils.wrapperIdling
import com.example.ceritaku.viewmodel.StoryViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import com.example.ceritaku.viewmodel.utils.PrefViewModelFactory
import com.example.ceritaku.viewmodel.utils.SettingPrefViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class MapsFragment : Fragment(){

    private val viewModel : StoryViewModel by viewModels{ VModelFactory.getInstance(requireActivity()) }
    private lateinit var prefViewModel : SettingPrefViewModel

    override fun onStart() {
        super.onStart()
        IdlingConfig.decrement()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        IdlingConfig.decrement()
        wrapperIdling {
            prefViewModel = ViewModelProvider(requireActivity(),
                PrefViewModelFactory(
                    UserPrefrences.getInstance(requireContext().dataStore)
                )
            )[SettingPrefViewModel::class.java]

            prefViewModel.getUserToken().observe(viewLifecycleOwner){
                lifecycleScope.launch {
                    getMapsStories("Bearer $it")
                }
            }
            return inflater.inflate(R.layout.fragment_maps, container, false)

        }

    }


    private suspend fun getMapsStories(token : String){
        IdlingConfig.decrement()
        viewModel.getMapsStories(1,token).observe(viewLifecycleOwner){respon ->
            when(respon){
                is MediatorResult.Loading->{
                    Log.d("lokasi","loading")
                }
                is MediatorResult.Sucess->{
                    Log.d("jumlah lokasi",respon.data.listStory.size.toString())
                    showMapStories(respon.data.listStory)
                    IdlingConfig.decrement()
                }
                is MediatorResult.Error->{
                    Log.d("lokasi","error")
                }
            }
        }
    }



    private fun showMapStories(listData : List<Story>){
        IdlingConfig.decrement()
            val callback = OnMapReadyCallback { googleMap ->
                IdlingConfig.decrement()
                listData.forEach { data->
                    IdlingConfig.increment()
                    val position = LatLng( data.lat.toDouble(), data.lon.toDouble())
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(data.name)
                    )
                    //todo maps window detail
                    googleMap.setOnInfoWindowClickListener {

                    }
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(position))
                }

                googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireActivity(),
                        R.raw.map_style
                    )
                )

                googleMap.uiSettings.apply {
                    isZoomControlsEnabled = true
                    isIndoorLevelPickerEnabled = true
                    isCompassEnabled = true
                    isMapToolbarEnabled = true
                }

                googleMap.uiSettings.isMyLocationButtonEnabled = true
                googleMap.isIndoorEnabled = true
            }
        IdlingConfig.decrement()
            val mapFragment = childFragmentManager.findFragmentById(R.id.mapstories) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
        IdlingConfig.decrement()
    }

    //TODO 1.2 Maps Detail Story
    private fun toDetailPage(data : Story){
        val bundle = Bundle()
        val fragment = DetailStoryFragment()
        bundle.putParcelable(ListStoryFragment.extra_key_detail,data)
        fragment.arguments = bundle
        val supFragment = requireActivity().supportFragmentManager
        supFragment.beginTransaction()
            .replace(R.id.fragmentviemain,fragment)
            .addToBackStack(null)
            .commit()
    }



}