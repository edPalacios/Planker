package com.epf.planker.modules.mainactivity.features.calendar


import com.epf.planker.redux.*
import kotlinx.coroutines.*
import kotlin.random.Random

typealias ScheduledDay = Pair<Int, List<String>> // scheduled things for 1 day

data class CalendarState(val dates: List<ScheduledDay>? = null) : State<CalendarState>

sealed class CalendarGet : Request.Get() {
    object ScheduledDays : CalendarGet()

}

sealed class CalendarRenderAction : RenderAction {
    object UpdateCalendarDates : CalendarRenderAction()
    object UpdateCurrentDay : CalendarRenderAction()
}

sealed class CalendarEffect : Effect {
    sealed class SchedulesEffect : CalendarEffect() {
        object Load : SchedulesEffect()
    }
}

sealed class InterpreterResult : Action {
    class OnSucceed(val payload: List<ScheduledDay>) : InterpreterResult()
}

object CalendarReducer : Reducer<CalendarState, Action, Effect> {

    override fun invoke(p1: CalendarState, p2: Action): Pair<CalendarState, Pair<Action, Effect>> {
        return when (p2) {
            CalendarGet.ScheduledDays -> {
                p1 to (LoadingRenderAction.ShowLoading to CalendarEffect.SchedulesEffect.Load)
            }
            is InterpreterResult.OnSucceed -> {
                p1.copy(dates = p2.payload) to (CalendarRenderAction.UpdateCurrentDay to NoEffect)
            }

            else -> p1 to (EndOfFlow to NoEffect)
        }
    }
}

object CalendarInterpreter : Interpreter<CalendarState, Effect, Action> {
    override fun invoke(p1: CalendarState, p2: Effect): Deferred<List<Action>> {
        return CoroutineScope(Dispatchers.IO).async {
                when (p2) {
                    CalendarEffect.SchedulesEffect.Load -> {
                     listOf(LoadingRenderAction.HideLoading, simulateMultipleLoading(), simulateMultipleLoading(), simulateMultipleLoading())
                    }
                    else -> listOf(EndOfFlow)
                }
        }
    }

    private suspend fun simulateMultipleLoading(): InterpreterResult.OnSucceed {
        val payload1 = simulateService("loading scheduled current day from network").await()
        val payload2 = simulateService("loading calendar next 10 days from db").await()
        val payload3 = simulateService("loading scheduled past 10 days from file system").await()

        val scheduled = 1 to listOf(payload1)
        val scheduled2 = 2 to listOf("$payload1 - $payload2")
        val scheduled3 = 3 to listOf("$payload3 - $payload1")

        return InterpreterResult.OnSucceed(listOf(scheduled, scheduled2, scheduled3))
    }


}

suspend fun <T> simulateService(value: T): Deferred<T> {
    val long = 1000L
    println("////// start loading from service for $long")
    delay(long)
    println("////// end loading from service: $value")
    return CompletableDeferred(value)
}