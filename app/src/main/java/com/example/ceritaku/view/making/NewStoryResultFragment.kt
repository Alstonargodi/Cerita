package com.example.ceritaku.view.making

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ceritaku.databinding.FragmentNewStoryResultBinding
import com.example.ceritaku.data.remote.utils.Result
import com.example.ceritaku.view.utils.reduceImageSize
import com.example.ceritaku.view.utils.rotateBitmap
import com.example.ceritaku.viewmodel.StoryViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class NewStoryResultFragment : Fragment() {
    private lateinit var binding : FragmentNewStoryResultBinding
    private var getFile: File? = null
    private val viewModel : StoryViewModel by viewModels{
        VModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewStoryResultBinding.inflate(layoutInflater)

        showResultCamera()

        binding.btnupload.setOnClickListener {
            lifecycleScope.launch {
                uploadResult()
            }
        }

        return binding.root
    }

    private fun showResultCamera(){
        val file = arguments?.getSerializable("picture") as ArrayList<File>
        getFile = file[0]
        val result = BitmapFactory.decodeFile(file[0].path)
        binding.imageView3.setImageBitmap(result)
    }


    private suspend fun uploadResult(){
        val desc = binding.descarea.text.toString()
        if (getFile != null){
            val tempFile = reduceImageSize(getFile as File)
            val description = desc
            val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val multiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                tempFile.name,
                requestFile
            )

            viewModel.postStory(multiPart,description,0f,0f, authKey).observe(viewLifecycleOwner){
                when(it){
                    is Result.Loading ->{
                        binding.proesetitle.text = "uploading"
                        binding.pgbarupload.visibility = View.VISIBLE
                    }
                    is Result.Sucess->{
                        binding.proesetitle.visibility = View.GONE
                        binding.pgbarupload.visibility = View.GONE
                        Toast.makeText(context,it.data.message,Toast.LENGTH_SHORT).show()
                    }
                    is Result.Error->{
                        binding.proesetitle.text = it.error
                        binding.pgbarupload.visibility = View.GONE
                        Toast.makeText(context,it.error,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }



    companion object{
        const val camera_result_code = 200
        const val authKey = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWp6b2pEWUd4NFV0cUs5clUiLCJpYXQiOjE2NDkzOTc3OTh9.VrMbbMLriptuq8rmNfBGA2VZ88CNVJ6hJm93IAdcg7k"
    }



}