package com.erolc.exbar.utils

import android.app.Activity
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.util.Pair
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


@RequiresApi(Build.VERSION_CODES.M)
internal fun Activity.getRootWindowInsets() = window.decorView.rootWindowInsets

internal val Activity.windowInsets @RequiresApi(Build.VERSION_CODES.M)
get() = WindowInsetsCompat.toWindowInsetsCompat(getRootWindowInsets())

internal val View.windowInsets @RequiresApi(Build.VERSION_CODES.M)
get() = WindowInsetsCompat.toWindowInsetsCompat(rootWindowInsets,this)


internal val Activity.contentView get() = window.decorView.findViewById<FrameLayout>(Window.ID_ANDROID_CONTENT)

internal val Activity.insetsController get() = WindowCompat.getInsetsController(window, contentView)
internal val Fragment.insetsController get() = activity?.insetsController
internal val View.insetsController get() = ViewCompat.getWindowInsetsController(this)

internal fun navigationBar() = WindowInsetsCompat.Type.navigationBars()


internal fun statusBar() = WindowInsetsCompat.Type.statusBars()


internal fun captionBar() = WindowInsetsCompat.Type.captionBar()


internal fun ime() = WindowInsetsCompat.Type.ime()


internal fun systemBar() = WindowInsetsCompat.Type.systemBars()



internal fun View.getPosition(): Pair<Int, Int> {
    val position = IntArray(2)
    getLocationInWindow(position)
    return Pair(position[0], position[1])
}

internal val screenHeight: Int get() = Resources.getSystem().displayMetrics.heightPixels

