package com.epf.planker.modules.mainactivity

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.epf.planker.R
import com.epf.planker.redux.CloseApp
import com.epf.planker.redux.MainActivityState
import com.epf.planker.redux.Store
import com.epf.planker.redux.Subscriber
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private val screenSubsbriber: Subscriber<MainActivityState> = { _, renderAction ->
        when (renderAction) {
            is CloseApp -> finish()
        }
    }

    private val state = MainActivityState()
    private val store = Store(
        MainActivityReducer,
        MainActivityInterpreter(NavigationManagerImpl(supportFragmentManager)),
        state,
        screenSubsbriber
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        store.run(MainActivityAction.NavigationAction.LaunchHomeTab)

        navigation.selectedItemId = R.id.navigation_home
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                store.run(MainActivityAction.NavigationAction.LaunchHomeTab)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_schedule -> {
                store.run(MainActivityAction.NavigationAction.LaunchScheduleTab)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_calendar -> {
                store.run(MainActivityAction.NavigationAction.LaunchCalendarTab)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onBackPressed() {
//        super.onBackPressed() // ignored:  we handle manually onBack
        store.run(MainActivityAction.NavigationAction.OnBack)
    }

    fun dispatchFromFragment() {
        store.run(MainActivityAction.NavigationAction.LaunchInTab(R.id.navigation_home))
    }
}


class StackParcelable() : LinkedList<Fragment.SavedState>(), Parcelable {

    constructor(parcel: Parcel) : this() {
        parcel.readList(this, Fragment.SavedState::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.createTypedArrayList(Fragment.SavedState.CREATOR)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StackParcelable> {
        override fun createFromParcel(parcel: Parcel): StackParcelable {
            return StackParcelable(parcel)
        }

        override fun newArray(size: Int): Array<StackParcelable?> {
            return arrayOfNulls(size)
        }
    }

}
