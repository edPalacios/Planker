package com.epf.planker.modules.mainactivity

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.SparseArray
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
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {

    private var savedStateSparseArray = SparseArray<StackParcelable>()
    private var currentNavigationTabId = R.id.navigation_home

    private var commitId = -1 // TODO refactor


    private val changeScreen: Subscriber<MainActivityState> = {
        if (it.navigation.finish) {
            finish()
        }
    }

    private val subscribers = listOf(changeScreen)
    private val state = MainActivityState()
    private val store = Store(
        MainActivityReducer,
        MainActivityInterpreter(NavigationManagerImpl(supportFragmentManager)),
        state,
        subscribers
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) { // TODO refactor
            val sparseParcelableArray =
                savedInstanceState.getSparseParcelableArray<StackParcelable>(SAVED_STATE_CONTAINER_KEY)
            if (sparseParcelableArray != null) {
                savedStateSparseArray = sparseParcelableArray
            }
            currentNavigationTabId = savedInstanceState.getInt(SAVED_STATE_CURRENT_TAB_KEY)
            commitId = savedInstanceState.getInt(SAVED_STATE_CURRENT_COMMIT_ID)
        }
        CoroutineScope(Dispatchers.Main).launch {
            store.dispatch(MainActivityAction.NavigationAction.LaunchHome())
        }
        navigation.selectedItemId = currentNavigationTabId
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                currentNavigationTabId = R.id.navigation_home
                CoroutineScope(Dispatchers.Main).launch {
                    store.dispatch(MainActivityAction.NavigationAction.LaunchHome())
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_schedule -> {
                currentNavigationTabId = R.id.navigation_schedule
                CoroutineScope(Dispatchers.Main).launch {
                    store.dispatch(MainActivityAction.NavigationAction.LaunchSchedule())
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_calendar -> {
                currentNavigationTabId = R.id.navigation_calendar
                CoroutineScope(Dispatchers.Main).launch {
                    store.dispatch(MainActivityAction.NavigationAction.LaunchCalendar())
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSparseParcelableArray(SAVED_STATE_CONTAINER_KEY, savedStateSparseArray)
        outState.putInt(SAVED_STATE_CURRENT_TAB_KEY, currentNavigationTabId)
        outState.putInt(SAVED_STATE_CURRENT_COMMIT_ID, commitId)
    }


    override fun onBackPressed() {
//        super.onBackPressed() // ignored:  we handle manually onBack
        CoroutineScope(Dispatchers.Main).launch {
            store.dispatch(MainActivityAction.NavigationAction.OnBack(currentNavigationTabId))
        }

    }


    fun dispatchFromFragment() {
        CoroutineScope(Dispatchers.Main).launch {
            store.dispatch(MainActivityAction.NavigationAction.LaunchHome(rootTabId = currentNavigationTabId))

        }
    }

    companion object {
        const val BACK_STACK_ROOT_TAG = "back_stack-root"

        const val SAVED_STATE_CONTAINER_KEY = "ContainerKey"
        const val SAVED_STATE_CURRENT_TAB_KEY = "CurrentTabKey"
        const val SAVED_STATE_CURRENT_COMMIT_ID = "CommitId"
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
