package com.epf.planker.redux

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.epf.planker.modules.mainactivity.features.home.HomeFragment

interface State<S>

data class MainActivityState(val screen: Screen? = null) : State<MainActivityState>

data class Screen(val fragment: Fragment = HomeFragment(), val tag: String = "", @IdRes val currentFragmentId: Int)


