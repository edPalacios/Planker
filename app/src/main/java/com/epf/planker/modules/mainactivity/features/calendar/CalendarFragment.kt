package com.epf.planker.modules.mainactivity.features.calendar

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.epf.planker.R
import com.epf.planker.modules.mainactivity.features.base.BaseFragment
import com.epf.planker.modules.mainactivity.features.home.HomeViewModel
import com.epf.planker.redux.Store
import kotlinx.android.synthetic.main.fragment_calendar.*


class CalendarFragment : BaseFragment() {
    override fun screenLayout(): Int = R.layout.fragment_calendar

    private val calendarViewModel by lazy {
        val store = Store(CalendarReducer, CalendarInterpreter, CalendarState())
        ViewModelProviders.of(this, CalendarViewModelFactory(store)).get(CalendarViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarViewModel.calendarLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "finish job schedule for ${it.dates?.size} days", Toast.LENGTH_LONG).show()
        })

        calendarViewModel.loadingStateLiveData.observe(viewLifecycleOwner, Observer {
            if(it) progress.visibility = VISIBLE else progress.visibility = GONE
        })

    }


}
