package com.epf.planker.subscribers

/**
 * (S) -> Void

The subscribers are called whenever the (S)tate is changed.
        */

typealias Subscriber<S> = (S) -> Unit
