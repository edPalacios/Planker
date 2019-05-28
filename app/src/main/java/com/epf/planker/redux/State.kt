package com.epf.planker.redux

import android.util.SparseArray
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.epf.planker.R
import com.epf.planker.modules.mainactivity.StackParcelable
import com.epf.planker.modules.mainactivity.features.home.HomeFragment

interface State<S>

data class MainActivityState(
    val screenMap: SparseArray<Set<Screen>> = SparseArray(),
    @IdRes val currentRootTabId: Int? = null,
    val navigation: Navigation = Navigation()
) : State<MainActivityState>


class Screen(
    val fragment: Fragment = HomeFragment(),
    val tag: String = "",
    @IdRes val fragmentId: Int
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Screen

        if (tag != other.tag) return false
        if (fragmentId != other.fragmentId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tag.hashCode()
        result = 31 * result + fragmentId
        return result
    }
}


data class Navigation(
    val savedState: SparseArray<StackParcelable> = SparseArray(),
    @IdRes val navigationTabId: Int = R.id.navigation_home,
    val commitId: Int = -1,
    val finish: Boolean = false
)

