package com.epf.planker

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.epf.planker.fragments.CalendarFragment
import com.epf.planker.fragments.HomeFragment
import com.epf.planker.fragments.ScheduleFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.selectedItemId = R.id.navigation_home
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        replaceFragment(HomeFragment(), "homeFragment")
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                replaceFragment(HomeFragment(), "homeFragment")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_schedule -> {
                replaceFragment(ScheduleFragment(), "scheduleFragment")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_calendar -> {
                replaceFragment(CalendarFragment(), "calendarFragment")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun replaceFragment(fragment: Fragment,  tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.scren_container, fragment, tag)
            .commit()
    }
}
