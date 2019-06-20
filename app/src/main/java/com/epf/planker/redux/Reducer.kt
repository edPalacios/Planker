package com.epf.planker.redux

/**
 * https://github.com/ChrisAU/FunctionalRedux
 * (inout S, A) -> E

The reducer takes a (S)tate and an (A)ction and returns an updated (S)tate and an (E)ffect.
 */
// State, Action, Effect
typealias Reducer<S, A, E> = (S, A) -> Pair<S, Pair<A, E>>

