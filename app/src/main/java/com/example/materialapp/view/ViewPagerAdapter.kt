package com.example.materialapp.view

import android.icu.util.Calendar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.*

class ViewPagerAdapter (fa: FragmentActivity): FragmentStateAdapter(fa) {

    val calendar = Calendar.getInstance()
    val TODAY = getDate(calendar,0)
    val YESTERDAY = getDate(calendar,1)
    val DAYBEFOREYESTERDAY = getDate(calendar,2)

    fun getDate(calendar: Calendar, daysAgo: Int): Date {
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return calendar.time
    }

    private val fragments = arrayOf(SpaceFragment(TODAY),SpaceFragment(YESTERDAY),SpaceFragment(DAYBEFOREYESTERDAY))

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]

}