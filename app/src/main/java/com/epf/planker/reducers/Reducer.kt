package com.epf.planker.reducers

import com.epf.planker.actions.Action
import com.epf.planker.actions.HomeActions
import com.epf.planker.effects.Effect
import com.epf.planker.effects.HomeEffect
import com.epf.planker.fragments.CalendarFragment
import com.epf.planker.fragments.HomeFragment
import com.epf.planker.fragments.ScheduleFragment
import com.epf.planker.store.state.HomeState
import com.epf.planker.store.state.Screen
import com.epf.planker.store.state.Workout

/**
 * https://github.com/ChrisAU/FunctionalRedux
 * (inout S, A) -> E

The reducer takes a (S)tate and an (A)ction and returns an updated (S)tate and an (E)ffect.
 */
// State, Action, Effect
typealias Reducer<S, A, E> = (S, A) -> Pair<S,E>

object HomeReducer : Reducer<HomeState, Action, Effect> {

    override fun invoke(p1: HomeState, p2: Action): Pair<HomeState,Effect> {
        return when (p2) {
            is HomeActions.HomeNavigation.LaunchSchedule -> {
                p1.copy(screen = Screen(ScheduleFragment(), "ScheduleFragment")) to Effect.None
            }
            is HomeActions.HomeNavigation.LaunchHome -> {
                p1.copy(screen = Screen(HomeFragment(), "ScheduleFragment")) to Effect.None
            }
            is HomeActions.HomeNavigation.LaunchCalendar -> {
                p1.copy(screen = Screen(CalendarFragment(), "ScheduleFragment")) to Effect.None
            }
            is HomeActions.HomeWorkout.GetWorkout -> {
                p1 to HomeEffect.HomeWorkoutEffect.Load
            }
            is HomeActions.Interpreter.OnSucceed -> p1.copy(workout = p2.workout) to Effect.None
            else -> p1 to Effect.None
        }
    }

}
