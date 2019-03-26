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
        class LaunchHome(@IdRes val id: Int = R.id.navigation_home) : NavigationAction()
        class LaunchCalendar(@IdRes val id: Int = R.id.navigation_calendar) : NavigationAction()
        class LaunchSchedule(@IdRes val id: Int = R.id.navigation_schedule) : NavigationAction()
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
                p1.copy(screen = Screen(ScheduleFragment(), "ScheduleFragment", p2.id)) to None
            }
            is MainActivityAction.NavigationAction.LaunchHome -> {
                p1.copy(screen = Screen(HomeFragment(), "HomeFragment", p2.id)) to None
            }
            is MainActivityAction.NavigationAction.LaunchCalendar -> {
                p1.copy(screen = Screen(CalendarFragment(), "CalendarFragment", p2.id)) to None
            }
            else -> p1 to None
        }
    }
}


object MainActivityInterpreter : Interpreter<MainActivityState, Effect, Action> {

    override fun invoke(p1: MainActivityState, p2: Effect): Deferred<List<Action>> {
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