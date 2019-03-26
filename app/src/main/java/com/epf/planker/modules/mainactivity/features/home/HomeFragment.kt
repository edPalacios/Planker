package com.epf.planker.modules.mainactivity.features.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.epf.planker.R
import com.epf.planker.modules.mainactivity.features.base.BaseFragment
import com.epf.planker.redux.Store
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment() {

    override fun screenLayout(): Int = R.layout.fragment_home

    private val homeViewModel by lazy {
        val store = Store(HomeReducer, HomeInterpreter, HomeState())
        ViewModelProviders.of(this, HomeViewModelFactory(store)).get(HomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.workoutLiveData.observe(viewLifecycleOwner, Observer {
            current_workout.text = it.name
        })

    }


}
