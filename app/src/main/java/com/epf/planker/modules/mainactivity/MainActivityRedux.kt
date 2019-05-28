package com.epf.planker.modules.mainactivity

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.epf.planker.R
import com.epf.planker.modules.mainactivity.features.calendar.CalendarFragment
import com.epf.planker.modules.mainactivity.features.home.HomeFragment
import com.epf.planker.modules.mainactivity.features.schedule.ScheduleFragment
import com.epf.planker.redux.*
import kotlinx.coroutines.*

sealed class FragmentAction : Action {
    object Finish : FragmentAction()
    class SaveState(val newSavedState: Triple<Fragment.SavedState?, Int, Int>) : FragmentAction()
}

sealed class MainActivityAction : Action {
    sealed class NavigationAction(@IdRes val id: Int, @IdRes val rootTabId: Int) : MainActivityAction() {
        class LaunchHome(@IdRes id: Int = R.id.navigation_home, @IdRes rootTabId: Int = R.id.navigation_home) :
            NavigationAction(id, rootTabId)

        class LaunchCalendar(@IdRes id: Int = R.id.navigation_calendar, @IdRes rootTabId: Int = R.id.navigation_calendar) :
            NavigationAction(id, rootTabId)

        class LaunchSchedule(@IdRes id: Int = R.id.navigation_schedule, @IdRes rootTabId: Int = R.id.navigation_schedule) :
            NavigationAction(id, rootTabId)

        class OnBack(@IdRes val tabToRemove: Int) : NavigationAction(-1, -1)
    }

}

sealed class MainActivityEffect : Effect {
    sealed class NavigationEffect : MainActivityEffect() {
        object ChangeToHomeScreen : NavigationEffect()
        object ChangeToScheduleScreen : NavigationEffect()
        object ChangeToCalendarScreen : NavigationEffect()
    }
}

sealed class FragmentEffect : Effect {
    object HandleForwardNavigation : Effect
    object HandleBackwardNavigation : Effect
}

object MainActivityReducer : Reducer<MainActivityState, Action, Effect> {

    private val reduce: (String, MainActivityState, MainActivityAction.NavigationAction) -> MainActivityState =
        { tag, state, action ->
            val fragment = getFragment(action.id)
            val screen = Screen(fragment, tag, action.id)
            val updatedMap = state.screenMap[action.rootTabId]?.plus(screen) ?: setOf(screen)
            state.screenMap.put(action.rootTabId, updatedMap)
            val newNavigation = state.navigation.copy(navigationTabId = action.rootTabId)
            state.copy(navigation = newNavigation)
        }

    override fun invoke(p1: MainActivityState, p2: Action): Pair<MainActivityState, Effect> {
        return when (p2) {
            is MainActivityAction.NavigationAction.LaunchSchedule -> reduce(
                "ScheduleFragment",
                p1,
                p2
            ) to FragmentEffect.HandleForwardNavigation
            is MainActivityAction.NavigationAction.LaunchHome -> reduce(
                "HomeFragment",
                p1,
                p2
            ) to FragmentEffect.HandleForwardNavigation
            is MainActivityAction.NavigationAction.LaunchCalendar -> reduce(
                "CalendarFragment", p1, p2
            ) to FragmentEffect.HandleForwardNavigation
            is MainActivityAction.NavigationAction.OnBack -> {
                val updatedMap = p1.screenMap[p2.tabToRemove].minus(p1.screenMap[p2.tabToRemove].last())
                p1.screenMap.put(p2.tabToRemove, updatedMap)
                p1 to FragmentEffect.HandleBackwardNavigation

            }

            is FragmentAction.SaveState -> {
                val (savedState, fragmentId, commitId) = p2.newSavedState

                p1.navigation.savedState.put(
                    p1.navigation.navigationTabId,
                    StackParcelable().apply {
                        savedState?.let {
                            add(it)
                        }
                    }
                )

                val navigation = p1.navigation.copy(navigationTabId = fragmentId, commitId = commitId)
                p1.copy(navigation = navigation) to None
            }

            is FragmentAction.Finish -> {
                p1.copy(navigation = p1.navigation.copy(finish = true)) to None
            }
            else -> p1 to None
        }
    }

    private fun getFragment(@IdRes id: Int) = when (id) {
        R.id.navigation_home -> HomeFragment()
        R.id.navigation_schedule -> ScheduleFragment()
        R.id.navigation_calendar -> CalendarFragment()
        else -> HomeFragment()
    }
}


class MainActivityInterpreter(private val navigationManager: NavigationManager) :
    Interpreter<MainActivityState, Effect, Action>, NavigationManager by navigationManager {

    override fun invoke(
        p1: MainActivityState,
        p2: Effect
    ): Deferred<List<Action>> {

        fun runGloalAsync(job: () -> Unit) = GlobalScope.async { job }

        return GlobalScope.async {
            when (p2) {
                is FragmentEffect.HandleForwardNavigation -> {
                    listOf(replace(p1))
                }
                is FragmentEffect.HandleBackwardNavigation -> listOf(onBack(p1))
                else -> {
                    listOf(Ignore)
                }
            }
        }
    }

}


interface NavigationManager {
    fun replace(state: MainActivityState): Action
    fun onBack(state: MainActivityState): Action
}

class NavigationManagerImpl(private val supportFragmentManager: FragmentManager) : NavigationManager {
    override fun onBack(state: MainActivityState): Action {
        return if (state.screenMap[state.navigation.navigationTabId].isNullOrEmpty()) {
            FragmentAction.Finish
        } else {
            replace(state)
        }

    }

    override fun replace(state: MainActivityState): Action {
        val screen = state.screenMap[state.navigation.navigationTabId].last()

        val findFragmentByTag = supportFragmentManager.findFragmentByTag(screen.tag)
        return if (findFragmentByTag == null) {
            val (toPersistFragmentState, commitId) = runBlocking(Dispatchers.Main) {
                val toPersistFragmentState = savedFragmentState(state)
                val toApplySavedState = state.navigation.savedState[screen.fragmentId]?.pollLast()
                screen.fragment.setInitialSavedState(toApplySavedState)
                val commitId = supportFragmentManager.beginTransaction()
                    .replace(R.id.screen_container, screen.fragment, screen.tag)
                    .addToBackStack(screen.tag)
                    .commit()
                toPersistFragmentState to commitId
            }
            FragmentAction.SaveState(Triple(toPersistFragmentState, screen.fragmentId, commitId))
        } else {
            supportFragmentManager.popBackStack(state.navigation.commitId, 0)
            supportFragmentManager.beginTransaction()
                .replace(R.id.screen_container, findFragmentByTag, screen.tag)
                .commit()
            Ignore
        }
    }

    private fun savedFragmentState(state: MainActivityState): Fragment.SavedState? {
        //todo this method returns StackParcelable with saved state and currentNavigationTabId
        val currentFragment = supportFragmentManager.findFragmentById(R.id.screen_container)
        return currentFragment?.let {
            supportFragmentManager.saveFragmentInstanceState(currentFragment)
        }
    }

}
