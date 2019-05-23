package com.epf.planker.redux

import android.util.SparseArray
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.epf.planker.modules.mainactivity.features.home.HomeFragment

interface State<S>

data class MainActivityState(val screenMap: SparseArray<Set<Screen>> = SparseArray(), @IdRes val currentTabId: Int?=  null) : State<MainActivityState>



class Screen(val fragment: Fragment = HomeFragment(),
                  val tag: String = "",
                  @IdRes val currentFragmentId: Int){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Screen

        if (tag != other.tag) return false
        if (currentFragmentId != other.currentFragmentId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tag.hashCode()
        result = 31 * result + currentFragmentId
        return result
    }
}

