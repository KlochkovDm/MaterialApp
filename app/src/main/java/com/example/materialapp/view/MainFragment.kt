package com.example.materialapp.view

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.lifecycle.Observer
import coil.load
import com.example.materialapp.MainActivity
import com.example.materialapp.R
import com.example.materialapp.databinding.MainFragmentBinding
import com.example.materialapp.viewmodel.PictureOfTheDayState
import com.example.materialapp.viewmodel.PictureOfTheDayViewModel
import java.util.*

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
        setBottomAppBar()
        val calendar = Calendar.getInstance()
        val TODAY = getDate(calendar,0)
        val YESTERDAY = getDate(calendar,1)
        val DAYBEFOREYESTERDAY = getDate(calendar,2)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            viewModel.sendServerRequest(TODAY)
        }

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }

        binding.chipsGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipToday -> {
                    binding.chipsGroup.check(R.id.chipToday)
                    viewModel.sendServerRequest(TODAY)

                }
                R.id.chipYesterday -> {
                    binding.chipsGroup.check(R.id.chipYesterday)
                    viewModel.sendServerRequest(YESTERDAY)

                }
                R.id.chipDayBeforeYesterday -> {
                    binding.chipsGroup.check(R.id.chipDayBeforeYesterday)
                    viewModel.sendServerRequest(DAYBEFOREYESTERDAY)
                }
                else -> viewModel.sendServerRequest(TODAY)
            }
        }

    }

    private fun renderData(state: PictureOfTheDayState)  {
        when (state) {
            is PictureOfTheDayState.Error -> {
                binding.imageView.load(R.drawable.ic_load_error_vector)
                Toast.makeText(requireContext(),getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
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

    fun getDate(calendar: Calendar, daysAgo: Int): Date {
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return calendar.time
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> BottomNavigationDrawerFragment().show(requireActivity().supportFragmentManager, "")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar() {
        val context = activity as MainActivity
        context.setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)
    }

}