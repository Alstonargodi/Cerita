package com.example.ceritaku.view.home.maps

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
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
import com.example.ceritaku.data.remote.utils.Result
import com.example.ceritaku.view.detail.DetailStoryFragment
import com.example.ceritaku.view.home.liststory.ListStoryFragment
import com.example.ceritaku.view.upload.InsertStoryFragment
import com.example.ceritaku.viewmodel.StoryViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import com.example.ceritaku.viewmodel.utils.PrefViewModelFactory
import com.example.ceritaku.viewmodel.utils.SettingPrefViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class MapsFragment : Fragment(){

    private val viewModel : StoryViewModel by viewModels{ VModelFactory.getInstance(requireActivity()) }
    private lateinit var prefViewModel : SettingPrefViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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


    private suspend fun getMapsStories(token : String){
        viewModel.getMapsStories(1,token).observe(viewLifecycleOwner){respon ->
            when(respon){
                is Result.Loading->{
                    Log.d("lokasi","loading")
                }
                is Result.Sucess->{
                    Log.d("jumlah lokasi",respon.data.listStory.size.toString())
                    showMapStories(respon.data.listStory)
                }
                is Result.Error->{
                    Log.d("lokasi","error")
                }
            }
        }
    }


    private fun showMapStories(listData : List<Story>){
        val callback = OnMapReadyCallback { googleMap ->

            listData.forEach { data->
                val position = LatLng( data.lat.toDouble(), data.lon.toDouble())
                googleMap.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(data.name)
                        .snippet(data.toString())
                )
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
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun toDetailPage(data : Story){
        val bundle = Bundle()
        val fragment = DetailStoryFragment()
        bundle.putParcelable(ListStoryFragment.extra_key_detail,data)
        fragment.arguments = bundle
        val supFragment = requireActivity().supportFragmentManager
        supFragment.beginTransaction()
            .replace(R.id.fragmentview,fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object{
        const val CURRENT_LOC_LONG = "value_long"
        const val CURRENT_LOC_LAT = "value_lat"
    }


}