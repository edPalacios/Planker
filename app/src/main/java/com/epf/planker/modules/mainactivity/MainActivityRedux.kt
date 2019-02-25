package com.epf.planker.modules.mainactivity

import com.epf.planker.modules.mainactivity.features.calendar.CalendarFragment
import com.epf.planker.modules.mainactivity.features.home.HomeFragment
import com.epf.planker.modules.mainactivity.features.schedule.ScheduleFragment
import com.epf.planker.redux.Action
import com.epf.planker.redux.Ignore
import com.epf.planker.redux.Effect
import com.epf.planker.redux.None
import com.epf.planker.redux.Interpreter
import com.epf.planker.redux.Reducer
import com.epf.planker.redux.MainActivityState
import com.epf.planker.redux.Screen
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

sealed class MainActivityAction : Action {
    sealed class NavigationAction : MainActivityAction() {
        object LaunchHome : NavigationAction()
        object LaunchCalendar : NavigationAction()
        object LaunchSchedule : NavigationAction()
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
                p1.copy(screen = Screen(ScheduleFragment(), "ScheduleFragment")) to None
            }
            is MainActivityAction.NavigationAction.LaunchHome -> {
                p1.copy(screen = Screen(HomeFragment(), "HomeFragment")) to None
            }
            is MainActivityAction.NavigationAction.LaunchCalendar -> {
                p1.copy(screen = Screen(CalendarFragment(), "CalendarFragment")) to None
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