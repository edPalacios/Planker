package com.epf.planker.modules.mainactivity.features.calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.epf.planker.redux.LoadingRenderAction
import com.epf.planker.redux.Request
import com.epf.planker.redux.Store
import com.epf.planker.redux.Subscriber
import kotlinx.coroutines.Job


class CalendarViewModel(private val store: Store<CalendarState>) : ViewModel() {

    val calendarLiveData = MutableLiveData<CalendarState>()
    val loadingStateLiveData = MutableLiveData<Boolean>()

    private val jobs: List<Job> = listOf()

    private val calendarStateSubscriber: Subscriber<CalendarState> = { state, renderAction ->
        when (renderAction) {
            LoadingRenderAction.ShowLoading -> {
                loadingStateLiveData.value = true
            }
            LoadingRenderAction.HideLoading -> {
                loadingStateLiveData.value = false
            }
            CalendarRenderAction.UpdateCalendarDates -> {
                calendarLiveData.value = state
            }

            CalendarRenderAction.UpdateCurrentDay -> {
                calendarLiveData.value = state
            }
        }
    }

    init {
        store.subscribe(calendarStateSubscriber)
        dispatchGetCalendarScheduledDaysAction()
    }

    private fun dispatchGetCalendarScheduledDaysAction() {
        jobs.plus(store.run(CalendarGet.ScheduledDays))
    }

    override fun onCleared() {
        super.onCleared()
        jobs.forEach { it.cancel() }
    }

}

class CalendarViewModelFactory(private val store: Store<CalendarState>) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            return CalendarViewModel(store) as T
        }
        throw IllegalArgumentException("Cannot create instance of $modelClass")
    }

}