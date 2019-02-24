package com.epf.planker.store.state

import androidx.fragment.app.Fragment
import com.epf.planker.fragments.HomeFragment
import java.util.*

interface State<S> {
    fun get(): S
    fun notifyChange(subscribers: List<(S) -> Unit>) = { subscribers.forEach { it(get()) } }
}

//object Empty : State<Empty> {
//    override fun get(): Empty = this
//}

data class HomeState(
    val screen: Screen? = null,
    val workout : Workout? = null
) : State<HomeState> {
    override fun get(): HomeState  = this

}

data class Screen(val fragment: Fragment = HomeFragment(), val tag: String = "")

//interface Workout{
//    val isComlete: Boolean
//}

data class Workout(val id: Int = -1, val name: String = "", val isComlete: Boolean = false)  {


}
