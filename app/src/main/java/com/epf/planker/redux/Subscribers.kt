package com.epf.planker.redux

/**
 * (S) -> Void

The subscribers are called whenever the (S)tate is changed.
 */

typealias Subscriber<S> = (S, Action) -> Unit
