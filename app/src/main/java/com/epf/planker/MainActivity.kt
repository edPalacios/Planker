package com.epf.planker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.epf.planker.actions.Action
import com.epf.planker.actions.HomeActions
import com.epf.planker.effects.Effect
import com.epf.planker.fragments.HomeFragment
import com.epf.planker.interpreter.HomeInterpreter
import com.epf.planker.reducers.HomeReducer
import com.epf.planker.store.Store
import com.epf.planker.store.state.HomeState
import com.epf.planker.subscribers.Subscriber
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val changeScreen: Subscriber<HomeState> = { replaceFragment(it.screen.fragment, it.screen.tag) }
    val subscribers = listOf<Subscriber<HomeState>>(changeScreen)
    val state = HomeState()
    val store = Store<HomeState, Action, Effect>(HomeReducer, subscribers, HomeInterpreter, state)

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
                GlobalScope.launch {
                    store.dispatch(HomeActions.HomeNavigation.LaunchHome)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_schedule -> {
                GlobalScope.launch {
                    store.dispatch(HomeActions.HomeNavigation.LaunchSchedule)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_calendar -> {
                GlobalScope.launch {
                    store.dispatch(HomeActions.HomeNavigation.LaunchCalendar)
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.scren_container, fragment, tag)
            .commit()
    }
}

