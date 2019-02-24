package com.epf.planker.modules.mainactivity.features.home

import android.os.Bundle
import android.view.View
import com.epf.planker.R
import com.epf.planker.modules.mainactivity.features.base.BaseFragment
import com.epf.planker.redux.Store
import com.epf.planker.redux.Subscriber
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment() {
    override fun screenLayout(): Int = R.layout.fragment_home

    private val homeStateSubscriber: Subscriber<HomeState> = {
        it.workout?.let { workout ->
            current_workout.text = workout.name
        }

    }
    private val subscribers = listOf(homeStateSubscriber)
    private val state = HomeState()
    private val store = Store(HomeReducer, subscribers, HomeInterpreter, state)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalScope.launch {
            store.dispatch(HomeActions.HomeWorkout.Get)
        }
    }

}
