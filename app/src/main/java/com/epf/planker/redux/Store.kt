package com.epf.planker.redux

import com.epf.planker.modules.mainactivity.MainActivityAction
import kotlinx.coroutines.*

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
    private val reducer: Reducer<S, A, E>,
    private val interpreter: Interpreter<S, E, A>,
    private var currentState: S,
    private var subscribers: List<Subscriber<S>> = emptyList()
) {

    private val notifyState: Subscriber<S> = { s -> subscribers.forEach { it(s) } }

    suspend fun dispatch(action: A) {
        if (action is Ignore) {
            return
        }
        val (newState, effect) = reducer(currentState, action)
        val actions = interpreter(newState, effect).await()
        actions.forEach {
            notifyState(newState)
            currentState = newState
            dispatch(it)
        }
    }

    fun subscribe(subscriber: (S) -> Unit) {
        subscribers += subscriber
//        subscriber(currentState) // TODO decide what to do with this
    }

    fun unSubscribe(subscriber: (S) -> Unit) {
        subscribers -= subscriber
    }

    fun run(action: A) = CoroutineScope(Dispatchers.Main).launch {
        dispatch(action)
    }
}


fun runSync(job: () -> Unit) = runBlocking {
    job()
}