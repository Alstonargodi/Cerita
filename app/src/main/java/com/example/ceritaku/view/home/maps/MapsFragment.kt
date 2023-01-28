package com.example.ceritaku.view.home.maps

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ceritaku.R
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.data.remote.utils.MediatorResult
import com.example.ceritaku.databinding.DetailAnnotationBinding
import com.example.ceritaku.databinding.FragmentMapsBinding
import com.example.ceritaku.view.detail.DetailStoryFragment
import com.example.ceritaku.view.home.liststory.ListStoryFragment
import com.example.ceritaku.view.utils.IdlingConfig
import com.example.ceritaku.view.utils.wrapperIdling
import com.example.ceritaku.viewmodel.StoryViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import com.example.ceritaku.viewmodel.SettingPrefViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import kotlinx.coroutines.launch

class MapsFragment : Fragment(){
    private lateinit var binding : FragmentMapsBinding
    private lateinit var mapBinding : DetailAnnotationBinding
    private val viewModel : StoryViewModel by viewModels{ VModelFactory.getInstance(requireActivity()) }
    private val prefViewModel : SettingPrefViewModel by viewModels{ VModelFactory.getInstance(requireActivity()) }
    private var mapView : MapView? = null
    private lateinit var viewAnnotation : View
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
        mapBinding = DetailAnnotationBinding.inflate(layoutInflater)

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
        val annotationApi = mapView?.annotations
        val pointAnnotationManager = annotationApi?.createCircleAnnotationManager(mapView!!)
        IdlingConfig.decrement()
            listData.forEach { data ->
                val pointAnnotaionOptions : CircleAnnotationOptions = CircleAnnotationOptions()
                    .withPoint(Point.fromLngLat(
                        data.lon.toDouble(),
                        data.lat.toDouble()
                    ))
                    .withCircleRadius(8.0)
                    .withCircleColor("#ee4e8b")
                    .withCircleStrokeWidth(2.0)
                    .withCircleStrokeColor("#ffffff")
                pointAnnotationManager?.apply {
                    addClickListener(OnCircleAnnotationClickListener{
//                        toDetailPage(data)
                        true
                    })
                }
                showAnnotation(data)
                val cameraPosition = CameraOptions.Builder()
                    .center(Point.fromLngLat(
                        data.lon.toDouble(),
                        data.lat.toDouble()
                    ))
                    .build()
                mapView?.getMapboxMap()?.setCamera(cameraPosition)
//                pointAnnotationManager?.create(pointAnnotaionOptions)
            }
        FragmentMapsBinding.bind(viewAnnotation)
    }

    private fun showAnnotation(data : Story){
        viewAnnotation = binding.mapstories.viewAnnotationManager.addViewAnnotation(
            resId = R.layout.detail_annotation,
            options = viewAnnotationOptions {
                geometry(Point.fromLngLat(
                data.lon.toDouble(),
                data.lat.toDouble()
            ))
                anchor(ViewAnnotationAnchor.BOTTOM)
            },
        )
        viewAnnotation.visibility = View.VISIBLE
        val titleView = viewAnnotation.findViewById<TextView>(R.id.tv_anot)
        titleView.text = "a"

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