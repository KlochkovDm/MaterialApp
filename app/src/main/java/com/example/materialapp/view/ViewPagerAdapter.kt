package com.example.materialapp.view

import android.icu.util.Calendar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.materialapp.repository.PictureOfTheDayAPI
import java.util.*

class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val fragments =
        arrayOf(SpaceFragment(), SpaceFragment(), SpaceFragment())

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]

}