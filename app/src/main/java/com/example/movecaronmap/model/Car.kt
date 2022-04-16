package com.example.movecaronmap.model

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.movecaronmap.R

class Car(private var carActions: ICarActions) {
    fun setCarIcon() : Bitmap {
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = 4
        return BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.car, bitmapOptions)
    }

    fun stop() {
        carActions.stop()
    }
}