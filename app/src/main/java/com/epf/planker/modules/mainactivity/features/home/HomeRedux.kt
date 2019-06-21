package com.epf.planker.modules.mainactivity.features.home

import android.util.Log
import com.epf.planker.redux.*
import kotlinx.coroutines.*

data class HomeState(val workout: Workout? = null) : State<HomeState>

data class Workout(val id: Int = -1, val name: String = "", val isComlete: Boolean = false)


sealed class HomeActions : Action {

    sealed class HomeWorkout : HomeActions() {
        object Get : HomeWorkout()
        object Post : HomeWorkout()
        object Put : HomeWorkout()
    }

    sealed class Interpreter : HomeActions() {
        class OnSucceed(val workout: Workout) : Interpreter()
    }

}

sealed class HomeRenderAction : RenderAction {
    object UpdateWorkout : HomeRenderAction()
}


sealed class HomeEffect : Effect {

    sealed class HomeWorkoutEffect : HomeEffect() {
        object Load : HomeWorkoutEffect()
        object Save : HomeWorkoutEffect()
        object Delete : HomeWorkoutEffect()
    }
}


object HomeReducer : Reducer<HomeState, Action, Effect> {

    override fun invoke(p1: HomeState, p2: Action): Pair<HomeState, Pair<Action, Effect>> {
        return when (p2) {

            is HomeActions.HomeWorkout.Get -> {
                p1 to (EndOfFlow to HomeEffect.HomeWorkoutEffect.Load)
            }
            is HomeActions.Interpreter.OnSucceed -> {
                val state = p1.copy(workout = p2.workout)
                state to (HomeRenderAction.UpdateWorkout to NoEffect)
            }
            else -> p1 to (EndOfFlow to NoEffect)
        }
    }

}


object HomeInterpreter : Interpreter<HomeState, Effect, Action> {

    override fun invoke(p1: HomeState, p2: Effect): Deferred<List<Action>> {
        return CoroutineScope(Dispatchers.IO).async {
            Log.d("///////", "Post execution thread:" + Thread.currentThread().name)
            listOf(
                when (p2) {
                    is HomeEffect.HomeWorkoutEffect.Load -> {
                        println("////// start loading workout")
                        delay(3000)
                        println("////// end loading workout")
                        HomeActions.Interpreter.OnSucceed(
                            Workout(
                                1,
                                "Plank workout for Yuka",
                                false
                            )
                        )
                    }
                    else -> {
                        EndOfFlow
                    }
                }
            )

        }
    }

}
