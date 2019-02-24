package com.epf.planker.reducers

import com.epf.planker.actions.Action
import com.epf.planker.actions.HomeActions
import com.epf.planker.effects.Effect
import com.epf.planker.fragments.CalendarFragment
import com.epf.planker.fragments.HomeFragment
import com.epf.planker.fragments.ScheduleFragment
import com.epf.planker.store.state.HomeState
import com.epf.planker.store.state.Screen

/**
 * https://github.com/ChrisAU/FunctionalRedux
 * (inout S, A) -> E

The reducer takes a (S)tate and an (A)ction and returns an updated (S)tate and an (E)ffect.
 */
// State, Action, Effect
typealias Reducer<S, A, E> = (S, A) -> E

object HomeReducer : Reducer<HomeState, Action, Effect> {

    override fun invoke(p1: HomeState, p2: Action): Effect {
        return when (p2) {
            is HomeActions.HomeNavigation.LaunchSchedule -> {
                p1.screen = Screen(ScheduleFragment(), "ScheduleFragment"); Effect.None
            }
            is HomeActions.HomeNavigation.LaunchHome -> {
                p1.screen = Screen(HomeFragment(), "HomeFragment"); Effect.None
            }
            is HomeActions.HomeNavigation.LaunchCalendar -> {
                p1.screen = Screen(CalendarFragment(), "CalendarFragment"); Effect.None
            }
            else -> Effect.None
        }
    }

}
