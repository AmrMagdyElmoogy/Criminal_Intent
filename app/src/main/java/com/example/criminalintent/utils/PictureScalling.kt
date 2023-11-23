package com.example.criminalintent.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.math.roundToInt

fun getScaleBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {

    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)

    val srcWidth = options.outWidth.toFloat()
    val srcHeight = options.outHeight.toFloat()

    val smapleSize = if (srcWidth <= destWidth && srcHeight <= destHeight) {
        1
    } else {
        val width = srcWidth / destWidth
        val height = srcHeight / destHeight

        minOf(width, height).roundToInt()
    }
    return BitmapFactory.decodeFile(path, BitmapFactory.Options().apply {
        inSampleSize = smapleSize
    })
}