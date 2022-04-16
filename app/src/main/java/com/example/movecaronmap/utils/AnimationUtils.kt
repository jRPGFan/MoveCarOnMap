package com.example.movecaronmap.utils

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

object AnimationUtils {
    fun carAnimator(): ValueAnimator {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = 3000
        valueAnimator.interpolator = LinearInterpolator()
        return valueAnimator
    }
}