package com.epf.planker.redux

import kotlinx.coroutines.Deferred

/*
* * The interpreter takes the current (S)tate and an (E)ffect and returns an array of (A)ctions.
 * This is the only place side effects can actually occur, up until this point everything is an 'intent' to do something.
 * This is where you would implement storage, logging, network requests, timers, etc...
 *  */
typealias Interpreter<S, E, A> = (S, E) -> Deferred<List<A>>
