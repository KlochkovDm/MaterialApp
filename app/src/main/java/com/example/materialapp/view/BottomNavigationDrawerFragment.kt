package com.example.materialapp.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.example.materialapp.Parameters
import com.example.materialapp.R
import com.example.materialapp.databinding.BottomNavigationLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    private var _binding: BottomNavigationLayoutBinding? = null

    private val binding : BottomNavigationLayoutBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomNavigationLayoutBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.theme_main -> {
                    Parameters.getInstance().theme = R.style.Theme_MaterialApp
                    requireActivity().recreate()
                }
                R.id.theme_custom -> {
                    Parameters.getInstance().theme = R.style.CustomTheme
                    requireActivity().recreate()
                }
                R.id.theme_night -> {
                    val currentNightMode =
                        resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                    when (currentNightMode) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        } // Night mode is not active, we're using the light theme
                        Configuration.UI_MODE_NIGHT_YES -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        } // Night mode is active, we're using dark theme
                    }
                }
            }
            dismiss()
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}