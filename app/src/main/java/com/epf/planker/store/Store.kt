package com.epf.planker.store

import com.epf.planker.actions.Action
import com.epf.planker.interpreter.Interpreter
import com.epf.planker.reducers.Reducer
import com.epf.planker.store.state.State
import com.epf.planker.subscribers.Subscriber

/**
 * The store holds the latest version of the (S)tate, dispatches (A)ctions to the reducer,
 * notifies subscribers when the (S)tate changes, and calls the interpreter with any (E)ffects that are produced.
 *
 * Interpreter
 * (S, E) -> Future<[A]>
 * The interpreter takes the current (S)tate and an (E)ffect and returns an array of (A)ctions.
 * This is the only place side effects can actually occur, up until this point everything is an 'intent' to do something.
 * This is where you would implement storage, logging, network requests, timers, etc...
 */
class Store<S : State<S>, A, E>(
    val reducer: Reducer<S, A, E>,
    val subscribers: List<Subscriber<S>>,
    val interpreter: Interpreter<S, E, A>,
    val currentState: S
) {

    val notifyState = { subscribers.forEach { it(currentState) } }
    val notifyAction = { }

    suspend fun dispatch(action: A) {
        if (action is Action.Ignore) {
            return
        }
        val effect = reducer(currentState, action)
        val actions = interpreter(currentState, effect).await()
        actions.forEach {
            notifyAction()
            notifyState()
            dispatch(it)
        }
    }

    fun subscribe(subscriber: (S) -> Unit) {
        subscribers.plus(subscriber)
        subscriber(currentState)
    }

    fun unSubscribe(subscriber: (S) -> Unit) {
        subscribers.minus(subscriber)
    }
}

