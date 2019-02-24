package com.epf.planker.interpreter

import com.epf.planker.actions.Action
import com.epf.planker.effects.Effect
import com.epf.planker.effects.HomeEffect
import com.epf.planker.store.state.HomeState
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

/*
* * The interpreter takes the current (S)tate and an (E)ffect and returns an array of (A)ctions.
 * This is the only place side effects can actually occur, up until this point everything is an 'intent' to do something.
 * This is where you would implement storage, logging, network requests, timers, etc...
 *  */
typealias Interpreter<S, E, A> = (S, E) -> Deferred<List<A>>

object HomeInterpreter : Interpreter<HomeState, Effect, Action> {

    override fun invoke(p1: HomeState, p2: Effect): Deferred<List<Action>> { // TODO return pair of state and alist of actoins
        return GlobalScope.async {
            listOf( // TODO this is wrong
                when (p2) {
                    is HomeEffect.HomeNavigationEffect,
                    is HomeEffect.HomeNavigationEffect.ChangeToHomeScreen,
                    is HomeEffect.HomeNavigationEffect.ChangeToScheduleScreen -> Action.Ignore
                    else -> {
                        Action.Ignore
                    }
                }
            )

        }
    }

}
