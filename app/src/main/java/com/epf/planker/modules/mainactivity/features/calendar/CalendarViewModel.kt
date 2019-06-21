package com.epf.planker.modules.mainactivity.features.calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.epf.planker.redux.Action
import com.epf.planker.redux.Effect
import com.epf.planker.redux.Store
import com.epf.planker.redux.Subscriber
import kotlinx.coroutines.Job


class CalendarViewModel(store: Store<CalendarState, Action, Effect>) : ViewModel() {

    private val calendarLiveData = MutableLiveData<CalendarState>()

    private var jobs: List<Job>? = null

    private val calendarStateSubscriber: Subscriber<CalendarState> = { state, action ->
        TODO("update ui not implemented")
    }

    init {
        store.subscribe(calendarStateSubscriber)
        dispatchGetCalendarScheduledDaystAction()
    }

    private fun dispatchGetCalendarScheduledDaystAction() {

    }

}

class CalendarViewModelFactory(private val store: Store<CalendarState, Action, Effect>) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            return CalendarViewModel(store) as T
        }
        throw IllegalArgumentException("Cannot create instance of $modelClass")
    }

}