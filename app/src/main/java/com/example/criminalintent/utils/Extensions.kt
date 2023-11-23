package com.example.criminalintent.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun Intent.canResolveAction(activity: Activity): Boolean {
    val packageManager = activity.packageManager
    return packageManager.resolveActivity(this, PackageManager.MATCH_DEFAULT_ONLY) != null
}

fun Bitmap.rotate(uri: Uri): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(90f)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Context.getUriFromFile(photoFile: File): Uri {
    return FileProvider.getUriForFile(
        this,
        AUTHORITY_FILE,
        photoFile
    )
}