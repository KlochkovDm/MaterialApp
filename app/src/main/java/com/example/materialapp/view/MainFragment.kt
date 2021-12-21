package com.example.materialapp.view

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import coil.load
import com.example.materialapp.R
import com.example.materialapp.databinding.MainFragmentBinding
import com.example.materialapp.viewmodel.PictureOfTheDayState
import com.example.materialapp.viewmodel.PictureOfTheDayViewModel

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null

    private val binding : MainFragmentBinding
        get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
        viewModel.sendServerRequest()

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
    }

    private fun renderData(state: PictureOfTheDayState) {
        when (state) {
            is PictureOfTheDayState.Error -> {
                binding.imageView.load(R.drawable.ic_load_error_vector)
                Toast.makeText(requireContext(),"Something went wrong. Please thy again",Toast.LENGTH_LONG)
            }
            is PictureOfTheDayState.Loading -> {
                binding.imageView.load(R.drawable.ic_no_photo_vector)
            }
            is PictureOfTheDayState.Success -> {
                val pictureOfTheDayResponseData = state.pictureOfTheDayResponseData
                val url = pictureOfTheDayResponseData.url
                binding.imageView.load(url) {
                    lifecycle(this@MainFragment)
                    error(R.drawable.ic_load_error_vector)
                    placeholder(R.drawable.ic_no_photo_vector)
                binding.includeBottomSheet.bottomSheetDescriptionHeader.text = pictureOfTheDayResponseData.title
                binding.includeBottomSheet.bottomSheetDescription.text = pictureOfTheDayResponseData.explanation
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}