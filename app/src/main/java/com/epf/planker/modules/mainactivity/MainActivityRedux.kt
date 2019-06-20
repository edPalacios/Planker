package com.epf.planker.modules.mainactivity

import androidx.annotation.IdRes
import androidx.core.util.contains
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
    class SaveState(val newSavedState: Pair<Fragment.SavedState?, Int>) : FragmentAction()
}

sealed class MainActivityAction : Action {
    sealed class NavigationAction(@IdRes val fragmentId: Int) :
        MainActivityAction() {
        object LaunchHomeTab : NavigationAction(R.id.navigation_home)
        object LaunchCalendarTab : NavigationAction(R.id.navigation_calendar)
        object LaunchScheduleTab : NavigationAction(R.id.navigation_schedule)
        object OnBack : NavigationAction(-1)
        class LaunchInTab(toOpenId: Int) : NavigationAction(toOpenId)
    }

}

sealed class MainActivityEffect : Effect {
    sealed class NavigationEffect : MainActivityEffect() {
        object HandleForwardNavigation : NavigationEffect()
        object HandleBackwardNavigation : NavigationEffect()
        object Finish : NavigationEffect()
    }
}

sealed class MainActivityRenderAction : RenderAction {
    object ShowLoading : MainActivityRenderAction()
    object UpdateUi : MainActivityRenderAction()
}

object MainActivityReducer : Reducer<MainActivityState, Action, Effect> {

    private val reduce: (String, MainActivityState, MainActivityAction.NavigationAction) -> Pair<MainActivityState, Pair<Action, MainActivityEffect.NavigationEffect>> =
        { tag, state, action ->
            val fragment = getFragment(action.fragmentId)
            val screen = Screen(fragment, tag, action.fragmentId)
            val currentTabId = when {
                state.screenMap.contains( state.navigation.navigationTabId) -> action.fragmentId
                else -> state.navigation.navigationTabId
            }
            val updatedMap = state.screenMap[currentTabId]?.plus(screen) ?: setOf(screen)
            state.screenMap.put(currentTabId, updatedMap)
            val newNavigation = state.navigation.copy(navigationTabId = currentTabId)
            state.copy(navigation = newNavigation) to (EndOfFlow to MainActivityEffect.NavigationEffect.HandleForwardNavigation)
        }

    override fun invoke(p1: MainActivityState, p2: Action): Pair<MainActivityState, Pair<Action, Effect>> {
        val navigationTabId = p1.navigation.navigationTabId
        return when (p2) {
            is MainActivityAction.NavigationAction.LaunchScheduleTab -> reduce("ScheduleFragment", p1, p2)
            is MainActivityAction.NavigationAction.LaunchHomeTab -> reduce("HomeFragment", p1, p2)
            is MainActivityAction.NavigationAction.LaunchCalendarTab -> reduce("CalendarFragment", p1, p2)
            is MainActivityAction.NavigationAction.LaunchInTab -> reduce("AnotherFragment", p1, p2)
            is MainActivityAction.NavigationAction.OnBack -> {
                val updatedMap = p1.screenMap[navigationTabId].minus(p1.screenMap[navigationTabId].last())
                p1.screenMap.put(navigationTabId, updatedMap)
                p1 to (EndOfFlow to MainActivityEffect.NavigationEffect.HandleBackwardNavigation)

            }

            is FragmentAction.SaveState -> {
                val (savedState, commitId) = p2.newSavedState
                p1.navigation.savedState.put(
                    navigationTabId,
                    StackParcelable().apply {
                        savedState?.let {
                            add(it)
                        }
                    }
                )

                val navigation = p1.navigation.copy(commitId = commitId)
                p1.copy(navigation = navigation) to (EndOfFlow to None)
            }

            is FragmentAction.Finish -> p1 to (EndOfFlow to None)
            else -> p1 to (EndOfFlow to None)
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
        return CoroutineScope(Dispatchers.IO).async {
            when (p2) {
                is MainActivityEffect.NavigationEffect.HandleForwardNavigation -> listOf(replace(p1))
                is MainActivityEffect.NavigationEffect.HandleBackwardNavigation -> listOf(onBack(p1))
                else -> listOf(EndOfFlow)
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
            CloseApp
        } else {
            replace(state)
        }

    }

    override fun replace(state: MainActivityState): Action {
        val screen = state.screenMap[state.navigation.navigationTabId].last()

        val findFragmentByTag = supportFragmentManager.findFragmentByTag(screen.tag)
        return if (findFragmentByTag == null) {
            val (toPersistFragmentState, commitId) = runBlocking(Dispatchers.Main) {
                val toPersistFragmentState = savedFragmentState()
                val toApplySavedState = state.navigation.savedState[screen.fragmentId]?.pollLast()
                screen.fragment.setInitialSavedState(toApplySavedState)
                val commitId = supportFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.screen_container, screen.fragment, screen.tag)
                    .addToBackStack(screen.tag)
                    .commit()
                toPersistFragmentState to commitId
            }
            FragmentAction.SaveState(toPersistFragmentState to commitId)
        } else {
            supportFragmentManager.popBackStack(state.navigation.commitId, 0)
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.screen_container, findFragmentByTag, screen.tag)
                .commit()
            EndOfFlow
        }
    }

    private fun savedFragmentState(): Fragment.SavedState? {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.screen_container)
        return currentFragment?.let {
            supportFragmentManager.saveFragmentInstanceState(currentFragment)
        }
    }

}
