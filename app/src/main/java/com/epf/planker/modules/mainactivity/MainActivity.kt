package com.epf.planker.modules.mainactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.epf.planker.R
import com.epf.planker.redux.MainActivityState
import com.epf.planker.redux.Store
import com.epf.planker.redux.Subscriber
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val changeScreen: Subscriber<MainActivityState> = {
        it.screen?.let { screen ->
            replaceFragment(screen.fragment, screen.tag)
        }

    }
    private val subscribers = listOf(changeScreen)
    private val state = MainActivityState()
    private val store = Store(
        MainActivityReducer,
        subscribers,
        MainActivityInterpreter,
        state
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.selectedItemId = R.id.navigation_home
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        GlobalScope.launch {
            store.dispatch(MainActivityAction.NavigationAction.LaunchHome)
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                CoroutineScope(Dispatchers.Main).launch {
                    store.dispatch(MainActivityAction.NavigationAction.LaunchHome)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_schedule -> {
                CoroutineScope(Dispatchers.Main).launch  {
                    store.dispatch(MainActivityAction.NavigationAction.LaunchSchedule)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_calendar -> {

                CoroutineScope(Dispatchers.Main).launch {
                    store.dispatch(MainActivityAction.NavigationAction.LaunchCalendar)
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.screen_container, fragment, tag)
            .commit()
    }

}

