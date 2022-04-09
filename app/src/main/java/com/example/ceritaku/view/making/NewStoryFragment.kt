package com.example.ceritaku.view.making

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ceritaku.MainActivity
import com.example.ceritaku.R
import com.example.ceritaku.databinding.FragmentInertNewStoryBinding
import com.example.ceritaku.view.utils.createFile
import java.io.File


class NewStoryFragment : Fragment() {
    private lateinit var binding : FragmentInertNewStoryBinding
    private var imageCapture : ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if (!allPermissiongranted()) {
                Toast.makeText(
                    context,
                    "Doens't have any permission",
                    Toast.LENGTH_SHORT
                ).show()

                requireActivity().finish()
            }
        }
    }

    private fun allPermissiongranted() = REQUIRED_PERMISSIONS.all{
        ContextCompat.checkSelfPermission(requireContext(),it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInertNewStoryBinding.inflate(layoutInflater)

        if (!allPermissiongranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnbackcamera.setOnClickListener {
            startActivity(Intent(context,MainActivity::class.java))
        }

        binding.btncapture.setOnClickListener { capturePhoto() }

        startCamera()

        return binding.root
    }


    private fun startCamera(){
        val cameraProvider = ProcessCameraProvider
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


    private fun capturePhoto(){
        val imageResult = imageCapture?:return
        val imageFile = createFile(requireActivity().application)

        val outputFiles = ImageCapture.OutputFileOptions.Builder(imageFile).build()
        imageResult.takePicture(
            outputFiles,
            ContextCompat.getMainExecutor(requireContext()),
            object :ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(
                        context,
                        "Sucess capturing",
                        Toast.LENGTH_SHORT
                    ).show()

                    val bundle = Bundle()
                    val fragment = NewStoryResultFragment()
                    val imageFiles = ArrayList<File>()
                    imageFiles.add(imageFile)
                    bundle.putSerializable("picture",imageFiles)

                    val fragmentManager = requireActivity().supportFragmentManager
                    fragment.arguments = bundle
                    fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentview,fragment)
                        .commit()


                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        context,
                        "Fail capturing",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        )
    }


    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}