package com.example.ceritaku.view.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ceritaku.MainActivity
import com.example.ceritaku.R
import com.example.ceritaku.data.remote.utils.MediatorResult
import com.example.ceritaku.databinding.FragmentInsertstoryBinding
import com.example.ceritaku.view.utils.IdlingConfig
import com.example.ceritaku.view.utils.Utils.reduceImageSize
import com.example.ceritaku.view.utils.wrapperIdling
import com.example.ceritaku.viewmodel.SettingPrefViewModel
import com.example.ceritaku.viewmodel.StoryViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.create
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.concurrent.TimeUnit


class InsertStoryFragment : Fragment(){

 
    private lateinit var binding : FragmentInsertstoryBinding
    private val viewModel : StoryViewModel by viewModels{ VModelFactory.getInstance(requireActivity()) }
    private val prefViewModel : SettingPrefViewModel by viewModels{ VModelFactory.getInstance(requireContext()) }

    private var getFile: File? = null
    private var userToken = ""
    private var curLongitude = 0F
    private var curlatitude = 0F


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        IdlingConfig.decrement()
        wrapperIdling {
            binding = FragmentInsertstoryBinding.inflate(layoutInflater)

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

            prefViewModel.getUserToken().observe(viewLifecycleOwner){ userToken = it }

            showResultCamera()

            return binding.root
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnupload.setOnClickListener {
            binding.pgbarupload.visibility = View.VISIBLE
            binding.proesetitle.visibility = View.VISIBLE
            showMessage("Uploading")
            lifecycleScope.launch {
                uploadResult()
            }
        }

        binding.btnbacktohome.setOnClickListener {
            backToHome()
        }

        binding.uploadlocation.setOnClickListener {
            createLocationRequest()
            "Finding..".also { binding.uploadlocation.text = it }
        }
    }

    private fun showResultCamera(){
        val arrayFile = arguments?.getSerializable("picture") as ArrayList<*>
        val file = arrayFile[0] as File
        getFile = file
        val result = BitmapFactory.decodeFile(file.path)
        binding.imageView3.setImageBitmap(result)
    }


    private suspend fun uploadResult(){
        IdlingConfig.increment()
        wrapperIdling {
            val desc = binding.descarea.text
                .toString()
                .toRequestBody("text/plain".toMediaType())

            if (getFile != null){
                val tempFile = reduceImageSize(getFile as File)
                val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val multiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    tempFile.name,
                    requestFile
                )


                viewModel.postStory(multiPart, desc, curlatitude, curLongitude,userToken).observe(viewLifecycleOwner){
                    when(it){
                        is MediatorResult.Loading ->{
                            binding.pgbarupload.visibility = View.VISIBLE
                            showMessage("Uploading")
                        }
                        is MediatorResult.Sucess->{
                            binding.pgbarupload.visibility = View.GONE
                            showMessage(it.data.message)
                            lifecycleScope.launch {
                                delay(2000L)
                                startActivity(Intent(requireContext(),MainActivity::class.java))
                                activity?.finishAffinity()
                            }
                        }
                        is MediatorResult.Error->{
                            binding.pgbarupload.visibility = View.GONE
                            showMessage(it.error + "try again")
                        }
                    }
                }
            }
        }

    }

    private fun backToHome(){
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager
            .beginTransaction()
            .replace(R.id.fragmentviemain,CameraFragment())
            .addToBackStack(null)
            .commit()

    }

    private fun showMessage(message : String){
        binding.apply {
            proesetitle.text = message
            proesetitle.visibility = View.VISIBLE
        }
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {}
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun createLocationRequest() {
        locationRequest = create().apply {
            interval = TimeUnit.SECONDS.toMillis(1)
            maxWaitTime = TimeUnit.SECONDS.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                getMyLastLocation()
            }
            .addOnFailureListener {
                "Fail to get location Try Again".also { binding.uploadlocation.text = it }
            }

    }

    private fun getMyLastLocation() {
        if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
            fusedLocationClient.lastLocation.addOnSuccessListener { location  ->
                if (location != null){
                    curLongitude = location.longitude.toFloat()
                    curlatitude = location.latitude.toFloat()
                    "Location has found $curlatitude - $curLongitude".also { binding.uploadlocation.text = it }
                }

            }
        } else {
            requestPermissionLauncher.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}