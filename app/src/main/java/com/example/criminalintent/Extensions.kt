package com.example.criminalintent

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager

fun Intent.canResolveAction(activity: Activity): Boolean {
    val packageManager = activity.packageManager
    return packageManager.resolveActivity(this, PackageManager.MATCH_DEFAULT_ONLY) != null
}

