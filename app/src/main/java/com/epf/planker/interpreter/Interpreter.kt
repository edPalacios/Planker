package com.epf.planker.interpreter

import com.epf.planker.actions.Action
import com.epf.planker.actions.HomeActions
import com.epf.planker.effects.Effect
import com.epf.planker.effects.HomeEffect
import com.epf.planker.store.state.HomeState
import com.epf.planker.store.state.Workout
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

/*
* * The interpreter takes the current (S)tate and an (E)ffect and returns an array of (A)ctions.
 * This is the only place side effects can actually occur, up until this point everything is an 'intent' to do something.
 * This is where you would implement storage, logging, network requests, timers, etc...
 *  */
typealias Interpreter<S, E, A> = (S, E) -> Deferred<List<A>>

object HomeInterpreter : Interpreter<HomeState, Effect, Action> {

    override fun invoke(p1: HomeState, p2: Effect): Deferred<List<Action>> {
        return GlobalScope.async {
            listOf( // TODO this is wrong
                when (p2) {
                    is HomeEffect.HomeNavigationEffect,
                    is HomeEffect.HomeNavigationEffect.ChangeToHomeScreen,
                    is HomeEffect.HomeNavigationEffect.ChangeToScheduleScreen -> Action.Ignore

                    is HomeEffect.HomeWorkoutEffect.Load -> {
                        println("////// start loading workout")
                        delay(3000)
                        println("////// end loading workout")
                        HomeActions.Interpreter.OnSucceed(Workout(1, "Plank workout for Yuka", false))
                    }
                    else -> {
                        Action.Ignore
                    }
                }
            )

        }
    }

}
