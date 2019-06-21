package com.epf.planker.modules.mainactivity.features.calendar

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.epf.planker.R
import com.epf.planker.modules.mainactivity.features.base.BaseFragment
import com.epf.planker.modules.mainactivity.features.home.HomeViewModel
import com.epf.planker.redux.Store


class CalendarFragment : BaseFragment() {
    override fun screenLayout(): Int = R.layout.fragment_calendar

    private val calendarViewModel by lazy {
        val store = Store(CalendarReducer, CalendarInterpreter, CalendarState())
        ViewModelProviders.of(this, CalendarViewModelFactory(store)).get(HomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarViewModel.workoutLiveData.observe(viewLifecycleOwner, Observer {
        })

    }


}
