package com.epf.planker.redux

/**
 * A description of an (A)ction which can be dispatched in the Store to the Reducer to mutate the (S)tate and/or execute (E)ffects.
 * An action for example handles click events: open new screen, press some button, etc
 * And effect is the result of the action: change screen, save data, load, etc
 */

interface Action

object Ignore : Action
