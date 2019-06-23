package com.epf.planker.modules.mainactivity.features.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.epf.planker.redux.Store
import com.epf.planker.redux.Subscriber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel(private val store: Store<HomeState>) : ViewModel() {

    val workoutLiveData = MutableLiveData<Workout>()

    lateinit var job: Job

    private val homeStateSubscriber: Subscriber<HomeState> = { state, renderAction ->
        when (renderAction) {
            is HomeRenderAction.UpdateWorkout -> {
                state.workout?.let { workout ->
                    workoutLiveData.value = workout
                }
            }
        }
    }

    init {
        store.subscribe(homeStateSubscriber)
        dispatchHomeWorkoutAction()
    }

    private fun dispatchHomeWorkoutAction() {
        job = CoroutineScope(Dispatchers.Main).launch {
            Log.d("///////", "Post execution thread:" + Thread.currentThread().name)
            store.dispatch(HomeActions.HomeWorkout.Get)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}


class HomeViewModelFactory(private val store: Store<HomeState>) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(store) as T
        }
        throw IllegalArgumentException("Cannot create instance of $modelClass")
    }

}