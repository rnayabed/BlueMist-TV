package com.anatame.pickaflix.utils

import android.content.res.Resources.getSystem

val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()
val Int.dp: Int get() = (this / getSystem().displayMetrics.density).toInt()