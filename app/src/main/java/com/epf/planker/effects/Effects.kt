package com.epf.planker.effects

/**
 * https://github.com/ChrisAU/FunctionalRedux
 * A description of a side effect that describes what task you want to perform.
 * Typical descriptions may include things such as 'save', 'log', 'load url', 'api', 'wait', etc...
 */
sealed class Effect {
    object None: Effect()
}

sealed class ViewEffect: Effect() {
    object UpdateUi: ViewEffect()
}
sealed class NavigationEffect: Effect() {
    object ChangeScreen: NavigationEffect()
}


sealed class DataEffect: Effect() {

    object Save : DataEffect()
    object Load: DataEffect()
}

sealed class HomeEffect : Effect() {
    sealed class HomeNavigationEffect : HomeEffect() {
        object ChangeToHomeScreen: HomeNavigationEffect()
        object ChangeToScheduleScreen: HomeNavigationEffect()
        object ChangeToCalendarScreen: HomeNavigationEffect()
    }

    sealed class HomeWorkoutEffect : HomeEffect() {
        object Load: HomeWorkoutEffect()
        object Save: HomeWorkoutEffect()
        object Delete: HomeWorkoutEffect()
    }
}