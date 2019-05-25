package com.epf.planker.modules.mainactivity

import androidx.annotation.IdRes
import com.epf.planker.R
import com.epf.planker.modules.mainactivity.features.calendar.CalendarFragment
import com.epf.planker.modules.mainactivity.features.home.HomeFragment
import com.epf.planker.modules.mainactivity.features.schedule.ScheduleFragment
import com.epf.planker.redux.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

sealed class MainActivityAction : Action {
    sealed class NavigationAction(@IdRes val id: Int, @IdRes val rootTabId: Int ) : MainActivityAction() {
        class LaunchHome(@IdRes id: Int = R.id.navigation_home, @IdRes  rootTabId: Int = R.id.navigation_home) : NavigationAction(id,rootTabId)
        class LaunchCalendar(@IdRes id: Int = R.id.navigation_calendar, @IdRes rootTabId: Int = R.id.navigation_calendar) : NavigationAction(id,rootTabId)
        class LaunchSchedule(@IdRes id: Int = R.id.navigation_schedule, @IdRes rootTabId: Int = R.id.navigation_schedule) : NavigationAction(id,rootTabId)
        class OnBack(@IdRes val tabToRemove: Int)   : NavigationAction(-1,-1)
    }

}

sealed class MainActivityEffect : Effect {
    sealed class NavigationEffect : MainActivityEffect() {
        object ChangeToHomeScreen : NavigationEffect()
        object ChangeToScheduleScreen : NavigationEffect()
        object ChangeToCalendarScreen : NavigationEffect()
    }

}

object MainActivityReducer : Reducer<MainActivityState, Action, Effect> {

    private val reduce: (String, MainActivityState, MainActivityAction.NavigationAction) -> MainActivityState = { tag, state, action ->
        val fragment =  getFragment(action.id)
        val screen = Screen(fragment, tag, action.id)
        val updatedMap = state.screenMap[action.rootTabId]?.plus(screen)?: setOf(screen)
        state.screenMap.put(action.rootTabId, updatedMap)
        state.copy(currentTabId = action.rootTabId)
    }

    override fun invoke(p1: MainActivityState, p2: Action): Pair<MainActivityState, Effect> {
        return when (p2) {
            is MainActivityAction.NavigationAction.LaunchSchedule -> reduce("ScheduleFragment", p1, p2) to None
            is MainActivityAction.NavigationAction.LaunchHome -> reduce("HomeFragment", p1, p2) to None
            is MainActivityAction.NavigationAction.LaunchCalendar -> reduce("CalendarFragment", p1, p2) to None
            is MainActivityAction.NavigationAction.OnBack -> {
                val updatedMap = p1.screenMap[p2.tabToRemove].minus(p1.screenMap[p2.tabToRemove].last())
                p1.screenMap.put(p2.tabToRemove, updatedMap)
                p1.copy(currentTabId = -1) to None

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


object MainActivityInterpreter : Interpreter<MainActivityState, Effect, Action> {

    override fun invoke(p1: MainActivityState, p2: Effect): Deferred<List<Action>> { // TODO this could return state to list of actions
        return GlobalScope.async {
            listOf( // TODO this is wrong
                when (p2) {
                    is MainActivityEffect.NavigationEffect.ChangeToCalendarScreen,
                    is MainActivityEffect.NavigationEffect.ChangeToHomeScreen,
                    is MainActivityEffect.NavigationEffect.ChangeToScheduleScreen -> Ignore

                    else -> {
                        Ignore
                    }
                }
            )

        }
    }

}