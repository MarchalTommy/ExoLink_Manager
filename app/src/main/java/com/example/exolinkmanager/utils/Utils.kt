package com.example.exolinkmanager.utils

import android.content.res.Resources

fun Float.dp(): Float = this * density + 0.5f

val density: Float
    get() = Resources.getSystem().displayMetrics.density