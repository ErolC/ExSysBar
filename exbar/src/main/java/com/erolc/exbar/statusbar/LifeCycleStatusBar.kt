package com.erolc.exbar.statusbar

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.erolc.exbar.systemBar.SystemBarFactory

class LifeCycleStatusBar(
    owner: LifecycleOwner,
    private val bar: StatusBar
) : StatusBar by bar {
    internal val exeBar = (if (bar is LifeCycleStatusBar) bar.bar else bar) as StatusBarImpl

    private var _background: Int? = bar.background
    private var contentColorIsDark: Boolean? = bar.colorIsDark
    private var isHide: Int = 3
    private var _invasion = false

    private val simpleName = owner.javaClass.simpleName
    private var isChange = false

    private val TAG = "ExStatusBar"


    init {
        owner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_RESUME) {
                    if (owner is Activity && isChange || owner is Fragment) {
                        //如果activity做出了改变，那么会先恢复fragment的bar再恢复activity的Bar，会先恢复statusBar再恢复NavigationBar
                        restore()
                    }
                } else if (event == Lifecycle.Event.ON_DESTROY) {
                    SystemBarFactory.clear(source.hashCode())
                }

            }
        })
    }

    override var background: Int
        get() = bar.background
        set(value) {
            _background = value
            bar.background = value
        }


    override fun hide(isAdapterBang: Boolean) {
        isHide = if (isAdapterBang) 1 else 2
        exeBar.hide(isAdapterBang)
    }

    override fun show() {
        isHide = 3
        exeBar.show()
    }

    override var toEdge: Boolean
        get() = bar.toEdge
        set(value) {
            _invasion = value
            bar.toEdge = value
            isHide = 0
        }


    /**
     * 对资源进行恢复
     */
    internal fun restore() {
        Log.d(
            TAG,
            "$simpleName  ${exeBar.javaClass.simpleName} restore: $toEdge "
        )
        if (isHide != 0) {
            if (isHide == 3) {
                exeBar.show()
                _background?.let {
                    exeBar.background = it
                    Log.d(
                        TAG,
                        " ${exeBar.javaClass.simpleName} restore setBackground: $it "
                    )
                }

            } else {
                Log.d(TAG, "restore hide:${isHide == 1}")
                exeBar.hide(isHide == 1)
            }
        } else {
            exeBar.toEdge = _invasion
            if (!_invasion) {
                _background?.let {
                    exeBar.background = it
                }
            }
        }
        contentColorIsDark?.let {
            exeBar.colorIsDark = it
            Log.d(TAG, "restore setTextColor: $it")
        }
    }


}