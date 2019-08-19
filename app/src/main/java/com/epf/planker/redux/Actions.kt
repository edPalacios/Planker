package com.epf.planker.redux

/**
 * A description of an (A)ction which can be dispatched in the Store to the Reducer to mutate the (S)tate and/or execute (E)ffects.
 * An action for example handles click events: open new screen, press some button, etc
 * And effect is the result of the action: change screen, save data, load, etc
 */

interface Action

object EndOfFlow : Action

object NoAction : Action

interface RenderAction : Action

sealed class LoadingRenderAction : RenderAction {
    object ShowLoading : LoadingRenderAction()
    object HideLoading : LoadingRenderAction()
}


object CloseApp : RenderAction

// REST actions
sealed class Request : Action {
    abstract class Get : Request()
    abstract class Post : Request()
    abstract class Put : Request()
}

sealed class Result<T> : Action {
    class OnSucceed<T>(val result: T) : Result<T>()
    class OnError<T>(val result: T) : Result<T>()
}

