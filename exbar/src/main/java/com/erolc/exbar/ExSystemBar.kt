package com.erolc.exbar

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.erolc.exbar.navigationbar.NavigationBar
import com.erolc.exbar.statusbar.StatusBar
import com.erolc.exbar.systemBar.SystemBar
import com.erolc.exbar.systemBar.SystemBarFactory

/**
 * StatusBar入口，注意：所属同一个Activity的所有fragment共用一个StatsBar对象。注意切换fragment时StatusBar样式的恢复
 *
 */
object ExSystemBar {

    internal val outNavEdgeViews = mutableListOf<View>()
    internal val outStatusEdgeViews = mutableListOf<View>()

    @JvmStatic
    fun create(owner: LifecycleOwner): SystemBar {
        return SystemBarFactory.create(owner)
    }

    @JvmStatic
    fun createStatusBar(owner: LifecycleOwner): StatusBar {
        return create(owner).getStatusBar()
    }

    @JvmStatic
    fun createNavigationBar(owner: LifecycleOwner): NavigationBar {
        return create(owner).getNavigationBar()
    }
}

