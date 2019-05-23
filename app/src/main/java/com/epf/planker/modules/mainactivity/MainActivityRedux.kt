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
    sealed class NavigationAction : MainActivityAction() {
        class LaunchHome(@IdRes val id: Int = R.id.navigation_home, @IdRes val rootTabId: Int = R.id.navigation_home) : NavigationAction()
        class LaunchCalendar(@IdRes val id: Int = R.id.navigation_calendar, @IdRes val rootTabId: Int = R.id.navigation_calendar) : NavigationAction()
        class LaunchSchedule(@IdRes val id: Int = R.id.navigation_schedule, @IdRes val rootTabId: Int = R.id.navigation_schedule) : NavigationAction()
        class OnBack(@IdRes val tabToRemove: Int)   : NavigationAction()
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

    override fun invoke(p1: MainActivityState, p2: Action): Pair<MainActivityState, Effect> {
        return when (p2) {
            is MainActivityAction.NavigationAction.LaunchSchedule -> {
                val fragment =  getFragment(p2.id)
                val screen = Screen(fragment, "ScheduleFragment", p2.id)
                val updatedMap = p1.screenMap[p2.rootTabId]?.plus(screen)?: setOf(screen)

                p1.screenMap.put(p2.rootTabId, updatedMap)

                p1.copy(currentTabId = p2.rootTabId) to None
            }
            is MainActivityAction.NavigationAction.LaunchHome -> {
                val fragment =  getFragment(p2.id)
                val screen = Screen(fragment, "HomeFragment", p2.id)
                val updatedMap = p1.screenMap[p2.rootTabId]?.plus(screen) ?: setOf(screen)
                p1.screenMap.put(p2.rootTabId, updatedMap)

                p1.copy(currentTabId = p2.rootTabId)  to None
            }
            is MainActivityAction.NavigationAction.LaunchCalendar -> {
                val fragment =  getFragment(p2.id)
                val screen = Screen(fragment, "CalendarFragment", p2.id)
                val updatedMap = p1.screenMap[p2.rootTabId]?.plus(screen)?: setOf(screen)
                p1.screenMap.put(p2.rootTabId, updatedMap)

                p1.copy(currentTabId = p2.rootTabId)  to None
            }

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