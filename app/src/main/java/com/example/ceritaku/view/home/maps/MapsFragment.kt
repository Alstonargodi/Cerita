package com.example.ceritaku.view.home.maps

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ceritaku.R
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.data.remote.utils.MediatorResult
import com.example.ceritaku.databinding.FragmentMapsBinding
import com.example.ceritaku.view.detail.DetailStoryFragment
import com.example.ceritaku.view.home.liststory.ListStoryFragment
import com.example.ceritaku.view.utils.IdlingConfig
import com.example.ceritaku.view.utils.wrapperIdling
import com.example.ceritaku.viewmodel.StoryViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import com.example.ceritaku.viewmodel.SettingPrefViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.scalebar.ScaleBarPlugin
import kotlinx.coroutines.launch

class MapsFragment : Fragment(){
    private lateinit var binding : FragmentMapsBinding
    private val viewModel : StoryViewModel by viewModels{ VModelFactory.getInstance(requireActivity()) }
    private val prefViewModel : SettingPrefViewModel by viewModels{ VModelFactory.getInstance(requireActivity()) }
    private var mapView : MapView? = null

    override fun onStart() {
        super.onStart()
        IdlingConfig.decrement()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(layoutInflater)
        mapView = binding.mapstories
        mapView?.getMapboxMap()?.apply {
            loadStyleUri(Style.DARK)
        }
        IdlingConfig.decrement()
        wrapperIdling {
            prefViewModel.getUserToken().observe(viewLifecycleOwner){
                lifecycleScope.launch {
                    getMapsStories(it)
                }
            }
        }
        return binding.root
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
        val annotaionApi = mapView?.annotations
        val pointAnnotaionManager = annotaionApi?.createCircleAnnotationManager(mapView!!)
        IdlingConfig.decrement()
            listData.forEach { data ->
                val pointAnnotaionOptions : CircleAnnotationOptions = CircleAnnotationOptions()
                    .withPoint(Point.fromLngLat(
                        data.lat.toDouble(),
                        data.lon.toDouble()
                    ))
                    .withCircleRadius(8.0)
                    .withCircleColor("#ee4e8b")
                    .withCircleStrokeWidth(2.0)
                    .withCircleStrokeColor("#ffffff")
                pointAnnotaionManager?.apply {
                    addClickListener(OnCircleAnnotationClickListener{
                        toDetailPage(data)
                        true
                    })
                }
                val cameraPosition = CameraOptions.Builder()
                    .center(Point.fromLngLat(
                        data.lat.toDouble(),
                        data.lon.toDouble()
                    ))
                    .build()


                mapView?.getMapboxMap()?.setCamera(cameraPosition)
                pointAnnotaionManager?.create(pointAnnotaionOptions)
            }
    }

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