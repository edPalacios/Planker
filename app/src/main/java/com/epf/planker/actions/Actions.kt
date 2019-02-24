package com.epf.planker.actions

/**
 * A description of an (A)ction which can be dispatched in the Store to the Reducer to mutate the (S)tate and/or execute (E)ffects.
 * An action for example handles click events: open new screen, press some button, etc
 * And effect is the result of the action: change screen, save data, load, etc
 */

sealed class Action {

    object Ignore : Action()

}

sealed class ViewActions : Action() {
    object Finish : ViewActions()
    object Click : ViewActions()

}


sealed class NavigationActions : Action() {
    sealed class LaunchScreen : NavigationActions()
}

sealed class HomeActions : Action() {

    sealed class HomeNavigation : HomeActions() {
        object LaunchHome : HomeNavigation()
        object LaunchCalendar : HomeNavigation()
        object LaunchSchedule : HomeNavigation()
    }

}