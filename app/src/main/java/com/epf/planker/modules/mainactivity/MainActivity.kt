package com.epf.planker.modules.mainactivity

import android.os.Bundle
import android.util.SparseArray
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.epf.planker.R
import com.epf.planker.redux.MainActivityState
import com.epf.planker.redux.Screen
import com.epf.planker.redux.Store
import com.epf.planker.redux.Subscriber
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private var savedStateSparseArray = SparseArray<Fragment.SavedState>()
    private var currentSelectItemId = R.id.navigation_home

    private var commitId = -1 // TODO refactor


    private val changeScreen: Subscriber<MainActivityState> = {
        it.screen?.let { screen ->
            replaceFragment(screen)
        }

    }
    private val subscribers = listOf(changeScreen)
    private val state = MainActivityState()
    private val store = Store(
        MainActivityReducer,
        MainActivityInterpreter,
        state,
        subscribers
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) { // TODO refactor
            val sparseParcelableArray =
                savedInstanceState.getSparseParcelableArray<Fragment.SavedState>(SAVED_STATE_CONTAINER_KEY)
            if (sparseParcelableArray != null) {
                savedStateSparseArray = sparseParcelableArray
            }
            currentSelectItemId = savedInstanceState.getInt(SAVED_STATE_CURRENT_TAB_KEY)
            commitId = savedInstanceState.getInt(SAVED_STATE_CURRENT_COMMIT_ID)
        }
        CoroutineScope(Dispatchers.Main).launch {
            store.dispatch(MainActivityAction.NavigationAction.LaunchHome())
        }
        navigation.selectedItemId = currentSelectItemId
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                CoroutineScope(Dispatchers.Main).launch {
                    store.dispatch(MainActivityAction.NavigationAction.LaunchHome())
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_schedule -> {
                CoroutineScope(Dispatchers.Main).launch {
                    store.dispatch(MainActivityAction.NavigationAction.LaunchSchedule())
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_calendar -> {

                CoroutineScope(Dispatchers.Main).launch {
                    store.dispatch(MainActivityAction.NavigationAction.LaunchCalendar())
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    // https://github.com/elye/demo_android_fragments_swapping/blob/master/app/src/main/java/com/elyeproj/bottombarfragmentsswitching/MainActivity.kt
    // https://proandroiddev.com/fragments-swapping-with-bottom-bar-ffbd265bd742
    private fun replaceFragment(screen: Screen) {
        val (fragment, tag, currentId) = screen
        val findFragmentByTag = supportFragmentManager.findFragmentByTag(tag)
        if (findFragmentByTag == null) {
            savedFragmentState(screen)
            fragment.setInitialSavedState(savedStateSparseArray[currentId])
            commitId = supportFragmentManager.beginTransaction()
                .replace(R.id.screen_container, fragment, tag)
                .addToBackStack(tag)
                .commit()
        } else {
            supportFragmentManager.popBackStack(commitId, 0)
            supportFragmentManager.beginTransaction().replace(R.id.screen_container, findFragmentByTag, tag).commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSparseParcelableArray(SAVED_STATE_CONTAINER_KEY, savedStateSparseArray)
        outState.putInt(SAVED_STATE_CURRENT_TAB_KEY, currentSelectItemId)
        outState.putInt(SAVED_STATE_CURRENT_COMMIT_ID, commitId)
    }


    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach { fragment ->
            if (fragment != null && fragment.isVisible) {
                with(fragment.childFragmentManager) {
                    if (backStackEntryCount > 0) {
                        popBackStack()
                        return
                    }
                }
            }
        }
        super.onBackPressed()
    }


    private fun savedFragmentState(screen: Screen) {
        val (_, _, currentId) = screen
        val currentFragment = supportFragmentManager.findFragmentById(R.id.screen_container)
        if (currentFragment != null) {
            savedStateSparseArray.put(
                currentSelectItemId,
                supportFragmentManager.saveFragmentInstanceState(currentFragment)
            )
        }
        currentSelectItemId = currentId
    }

    companion object {
        const val BACK_STACK_ROOT_TAG = "back_stack-root"

        const val SAVED_STATE_CONTAINER_KEY = "ContainerKey"
        const val SAVED_STATE_CURRENT_TAB_KEY = "CurrentTabKey"
        const val SAVED_STATE_CURRENT_COMMIT_ID = "CommitId"
    }
}

