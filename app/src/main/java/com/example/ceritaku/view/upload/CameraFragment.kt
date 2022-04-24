package com.example.ceritaku.view.upload

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ceritaku.MainActivity
import com.example.ceritaku.R
import com.example.ceritaku.databinding.FragmentCameraBinding
import com.example.ceritaku.view.utils.IdlingConfig
import com.example.ceritaku.view.utils.Utils.createFile
import com.example.ceritaku.view.utils.Utils.uriToFile
import com.example.ceritaku.view.utils.wrapperIdling
import java.io.File


class CameraFragment : Fragment() {

    private lateinit var binding : FragmentCameraBinding
    private var imageCapture : ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ respon->
        if (respon.resultCode == RESULT_OK){
            val selectedImg: Uri = respon.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())

            val bundle = Bundle()
            val fragment = InsertStoryFragment()
            val imageFiles = ArrayList<File>()
            imageFiles.add(myFile)
            bundle.putSerializable("picture",imageFiles)

            val fragmentManager = requireActivity().supportFragmentManager
            fragment.arguments = bundle
            fragmentManager
                .beginTransaction()
                .replace(R.id.fragmentviemain,fragment)
                .addToBackStack(null)
                .commit()
        }
    }
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
        wrapperIdling {
            binding = FragmentCameraBinding.inflate(layoutInflater)

            if (!allPermissiongranted()) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }

            startCamera()

            return binding.root
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wrapperIdling {
            binding.btnbackcamera.setOnClickListener {
                startActivity(Intent(context,MainActivity::class.java))
            }

            binding.btnRotate.setOnClickListener {
                cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                    CameraSelector.DEFAULT_FRONT_CAMERA
                else
                    CameraSelector.DEFAULT_BACK_CAMERA

                startCamera()
            }

            binding.btncapture.setOnClickListener {
                wrapperIdling {
                    capturePhoto()
                }
            }

            binding.btnpickimage.setOnClickListener {
                pickImageGallery()
            }
        }

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
                    cameraSelector,
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
        wrapperIdling {
            IdlingConfig.increment()
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
                        val fragment = InsertStoryFragment()
                        val imageFiles = ArrayList<File>()
                        imageFiles.add(imageFile)
                        bundle.putSerializable("picture",imageFiles)

                        val fragmentManager = requireActivity().supportFragmentManager
                        fragment.arguments = bundle
                        fragmentManager
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.fragmentviemain,fragment)
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

    }

    private fun pickImageGallery(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        launcherIntentGallery.launch(intent)
    }

    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}