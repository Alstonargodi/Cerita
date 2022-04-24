package com.example.ceritaku.view.upload

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.ceritaku.MainActivity
import com.example.ceritaku.R
import com.example.ceritaku.data.local.datastore.UserPrefrences
import com.example.ceritaku.data.local.datastore.dataStore
import com.example.ceritaku.data.remote.utils.MediatorResult
import com.example.ceritaku.databinding.FragmentInsertstoryBinding
import com.example.ceritaku.view.utils.IdlingConfig
import com.example.ceritaku.view.utils.Utils.reduceImageSize
import com.example.ceritaku.view.utils.Utils.rotateBitmap
import com.example.ceritaku.view.utils.wrapperIdling
import com.example.ceritaku.viewmodel.StoryViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import com.example.ceritaku.viewmodel.utils.PrefViewModelFactory
import com.example.ceritaku.viewmodel.utils.SettingPrefViewModel
import com.google.android.gms.maps.GoogleMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class InsertStoryFragment : Fragment(){

    //TODO 2.5 insert Current Location
    private lateinit var binding : FragmentInsertstoryBinding
    private val viewModel : StoryViewModel by viewModels{ VModelFactory.getInstance(requireActivity()) }
    private lateinit var prefViewModel : SettingPrefViewModel
    private var getFile: File? = null
    private var userToken = ""

    private lateinit var mMap : GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        IdlingConfig.decrement()
        wrapperIdling {
            binding = FragmentInsertstoryBinding.inflate(layoutInflater)

            prefViewModel = ViewModelProvider(requireActivity(),
                PrefViewModelFactory(UserPrefrences.getInstance(requireContext().dataStore))
            )[SettingPrefViewModel::class.java]


            prefViewModel.getUserToken().observe(viewLifecycleOwner){ userToken = "Bearer $it" }

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
    }

    private fun showResultCamera(){
        val arrayFile = arguments?.getSerializable("picture") as ArrayList<*>
        val file = arrayFile[0] as File
        getFile = file
        val result = rotateBitmap(BitmapFactory.decodeFile(file.path))
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


                viewModel.postStory(multiPart, desc, 0f, 0f,userToken).observe(viewLifecycleOwner){
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



    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }



}