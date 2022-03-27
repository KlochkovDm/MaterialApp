package com.example.materialapp.view

import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.example.materialapp.R
import com.example.materialapp.databinding.SpaceFragmentBinding
import com.example.materialapp.viewmodel.PictureOfTheDayState
import com.example.materialapp.viewmodel.PictureOfTheDayViewModel
import java.util.*

class SpaceFragment: Fragment() {

    private lateinit var fragmentDate : Date

    private fun getFragmentDate () : Date{
        val vp = requireActivity().findViewById(R.id.viewPager) as ViewPager2
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -vp.currentItem)
        return calendar.time
    }

    private var _binding: SpaceFragmentBinding? = null

    private val binding: SpaceFragmentBinding
        get() = _binding!!

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SpaceFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        fragmentDate = getFragmentDate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                viewModel.sendServerRequest(fragmentDate)
            }
        }
        viewModel.getData().observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
    }

    private fun renderData(state: PictureOfTheDayState) {
        when (state) {
            is PictureOfTheDayState.Error -> {
                binding.imageView.load(R.drawable.ic_load_error_vector)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.something_went_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }
            is PictureOfTheDayState.Loading -> {
                binding.imageView.load(R.drawable.ic_no_photo_vector)
            }
            is PictureOfTheDayState.Success -> {
                val pictureOfTheDayResponseData = state.pictureOfTheDayResponseData
                val url = pictureOfTheDayResponseData.url
                val mediaType = pictureOfTheDayResponseData.mediaType
                if (mediaType == "video") {
                    binding.imageView.load(R.drawable.ic_baseline_play_circle_outline)
                    binding.imageView.setOnClickListener {
                        startActivity(Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(url)
                        })
                    }
                    binding.includeBottomSheet.bottomSheetDescriptionHeader.text =
                        pictureOfTheDayResponseData.title
                    binding.includeBottomSheet.bottomSheetDescription.text =
                        pictureOfTheDayResponseData.explanation

                } else {
                    binding.imageView.load(url) {
                        lifecycle(this@SpaceFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                        binding.includeBottomSheet.bottomSheetDescriptionHeader.text =
                            pictureOfTheDayResponseData.title
                        binding.includeBottomSheet.bottomSheetDescription.text =
                            pictureOfTheDayResponseData.explanation
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}