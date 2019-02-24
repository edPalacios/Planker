package com.epf.planker.actions

import com.epf.planker.store.state.Workout

/**
 * A description of an (A)ction which can be dispatched in the Store to the Reducer to mutate the (S)tate and/or execute (E)ffects.
 * An action for example handles click events: open new screen, press some button, etc
 * And effect is the result of the action: change screen, save data, load, etc
 */

sealed class Action {

    object Ignore : Action()

    sealed class Interpreter {
        class Succeed()
    }

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

    sealed class HomeWorkout : HomeActions() {
        object GetWorkout: HomeWorkout()
        object PostWorkout: HomeWorkout()
        object PutWorkout: HomeWorkout()
    }

    sealed class Interpreter: HomeActions() {
        class OnSucceed(val workout: Workout): Interpreter()
    }

}