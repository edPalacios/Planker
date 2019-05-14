package com.epf.planker.modules.mainactivity.features.schedule

import android.os.Bundle
import android.view.View
import com.epf.planker.R
import com.epf.planker.modules.mainactivity.MainActivity
import com.epf.planker.modules.mainactivity.features.base.BaseFragment
import com.epf.planker.modules.mainactivity.features.home.HomeFragment
import com.epf.planker.redux.Screen
import kotlinx.android.synthetic.main.fragment_schedule.*


class ScheduleFragment : BaseFragment() {
    override fun screenLayout(): Int = R.layout.fragment_schedule

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_new_screen_button.setOnClickListener {
            val a  = activity as MainActivity
            a.replaceFragment(Screen(HomeFragment(), "another home fragment", R.id.navigation_home))
        }
    }

}
