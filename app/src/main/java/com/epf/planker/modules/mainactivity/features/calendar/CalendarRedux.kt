package com.epf.planker.modules.mainactivity.features.calendar

import com.epf.planker.redux.*
import kotlinx.coroutines.Deferred

data class CalendarState(val dates: List<Int>? = null) : State<CalendarState> {

}

object CalendarReducer : Reducer<CalendarState, Action, Effect> {

    override fun invoke(p1: CalendarState, p2: Action): Pair<CalendarState, Pair<Action, Effect>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}

object CalendarInterpreter : Interpreter<CalendarState, Effect, Action> {
    override fun invoke(p1: CalendarState, p2: Effect): Deferred<List<Action>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}