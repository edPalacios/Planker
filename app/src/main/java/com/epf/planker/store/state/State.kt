package com.epf.planker.store.state

import androidx.fragment.app.Fragment
import com.epf.planker.fragments.HomeFragment

interface State<S> {
    fun get(): S
    fun notifyChange(subscribers: List<(S) -> Unit>) = { subscribers.forEach { it(get()) } }
}

object Empty : State<Empty> {
    override fun get(): Empty = this
}

data class HomeState(var screen: Screen = Screen()) : State<HomeState> {
    override fun get(): HomeState  = this

}

data class Screen(val fragment: Fragment = HomeFragment(), val tag: String = "")
