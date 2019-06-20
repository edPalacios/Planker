package com.epf.planker.redux

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * The store holds the latest version of the (S)tate, dispatches (A)ctions to the reducer,
 * notifies uiSubscriber when the (S)tate changes, and calls the interpreter with any (E)ffects that are produced.
 *
 * Interpreter
 * (S, E) -> Future<[A]> (in this case we use a Deferred of something)
 * The interpreter takes the current (S)tate and an (E)ffect and returns an array of (A)ctions.
 * This is the only place side effects can actually occur, up until this point everything is an 'intent' to do something.
 * This is where you would implement storage, logging, network requests, timers, etc...
 */
class Store<S : State<S>, A : Action, E : Effect>(
    private val reducer: Reducer<S, A, E>,
    private val interpreter: Interpreter<S, E, A>,
    private var currentState: S,
    private var uiSubscriber: Subscriber<S>? = null
) {

    tailrec suspend fun dispatch(action: A, pendingActions: List<A>? = null) {
        if (action is EndOfFlow && pendingActions.isNullOrEmpty()) {
            return
        }

        val actionToProcess = if (action is EndOfFlow) pendingActions?.firstOrNull() ?: return else action

        val (newState, actionEffectsPair) = reducer(currentState, actionToProcess)

        val (immediateAction, effect) = actionEffectsPair
        uiSubscriber?.invoke(newState, immediateAction)

        val actions = interpreter(newState, effect).await()

        val (actionsToProcess, renderActions) = actions.partition { it !is RenderAction }
        renderActions.takeIf { it.isNotEmpty() }?.forEach { renderAction ->
            uiSubscriber?.invoke(newState, renderAction)
        }
        currentState = newState

        if (actionsToProcess.isNotEmpty()) {
            dispatch(actionsToProcess.first(), actionsToProcess.drop(1))
        }
    }

    fun subscribe(subscriber: Subscriber<S>) {
        uiSubscriber = subscriber
//        subscriber(currentState) // TODO decide what to do with this
    }
//
//    fun unSubscribe(subscriber: Subscriber<S>) {
//        uiSubscriber -= subscriber
//    }

    fun run(action: A) = CoroutineScope(Dispatchers.Main).launch {
        dispatch(action)
    }
}


fun runSync(job: () -> Unit) = runBlocking {
    job()
}