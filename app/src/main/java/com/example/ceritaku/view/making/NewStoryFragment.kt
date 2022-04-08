package com.example.ceritaku.view.making

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.ceritaku.MainActivity
import com.example.ceritaku.R
import com.example.ceritaku.databinding.FragmentInertNewStoryBinding
import com.example.ceritaku.view.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationMenuView


class NewStoryFragment : Fragment() {
    private lateinit var binding : FragmentInertNewStoryBinding
    private var imageCapture : ImageCapture? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInertNewStoryBinding.inflate(layoutInflater)


        binding.btnbackcamera.setOnClickListener {
            startActivity(Intent(context,MainActivity::class.java))
        }


        startCamera()



        return binding.root
    }


    private fun startCamera(){
        var cameraProvider = ProcessCameraProvider
            .getInstance(requireContext())

        cameraProvider.addListener({
            val cameraProv : ProcessCameraProvider = cameraProvider.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewFInder.surfaceProvider)
                }


            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProv.unbindAll()
                cameraProv.bindToLifecycle(
                    viewLifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture
                )
            }catch (e : Exception){
                Toast.makeText(
                    context,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },ContextCompat.getMainExecutor(requireContext()))


    }

}