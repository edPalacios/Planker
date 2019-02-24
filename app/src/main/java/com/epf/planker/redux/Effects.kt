package com.epf.planker.redux

/**
 * https://github.com/ChrisAU/FunctionalRedux
 * A description of a side effect that describes what task you want to perform.
 * Typical descriptions may include things such as 'save', 'log', 'load url', 'api', 'wait', etc...
 */
interface Effect

object None : Effect

sealed class DataEffect : Effect {

    object Save : DataEffect()
    object Load : DataEffect()
}

