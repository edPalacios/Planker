package com.epf.planker.modules.mainactivity.features.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment() : Fragment() {
    abstract fun screenLayout(): Int


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(screenLayout(), container, false) ?: super.onCreateView(inflater, container, savedInstanceState)
    }


}