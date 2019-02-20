package com.epf.planker.fragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.epf.planker.R

abstract class BaseFragment() : Fragment() {
    abstract fun screenLayout(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(screenLayout(), container, false)

    }
}

class ScheduleFragment() : BaseFragment() {
    override fun screenLayout(): Int = R.layout.fragment_schedule

}

class HomeFragment() : BaseFragment() {
    override fun screenLayout(): Int = R.layout.fragment_home

}

class CalendarFragment() : BaseFragment() {
    override fun screenLayout(): Int = R.layout.fragment_calendar

}