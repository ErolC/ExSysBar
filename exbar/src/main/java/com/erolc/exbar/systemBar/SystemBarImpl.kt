package com.erolc.exbar.systemBar

import android.annotation.SuppressLint
import androidx.core.graphics.Insets
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.FragmentActivity
import com.erolc.exbar.model.TypeMask
import com.erolc.exbar.navigationbar.LifeCycleNavBar
import com.erolc.exbar.navigationbar.NavigationBar
import com.erolc.exbar.statusbar.LifeCycleStatusBar
import com.erolc.exbar.statusbar.StatusBar
import com.erolc.exbar.utils.*
import com.erolc.exbar.utils.contentView

@SuppressLint("ClickableViewAccessibility")
class SystemBarImpl(
    private val activity: FragmentActivity,
    private val navBar: LifeCycleNavBar,
    private val statusBar: LifeCycleStatusBar
) : SystemBar {

    internal var isNavVisible = true
    internal var isStatusVisible = true
    internal var toStatusEdge = false
    internal var toNavEdge = false

    private var animCallBack: ((Int) -> Unit)? = null
    private var type: TypeMask = TypeMask.IME

    init {
        statusBar.exeBar.setSystemBar(this)
        navBar.exeBar.setSystemBar(this)
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        activity.contentView.applyWindowInsetsListener { view, windowInsetsCompat ->

            val hasNotchInScreen = DisplayCutoutHandler.checkHasNotchInScreen(activity)
            if (statusBar.exeBar.isAdapterBang != null) {
                toStatusEdge = !(hasNotchInScreen && statusBar.exeBar.isAdapterBang == false)
                statusBar.exeBar.isAdapterBang = null
            }

            val navBar = windowInsetsCompat.getInsets(navigationBar())
            val statusBar = windowInsetsCompat.getInsets(statusBar())
            val sysBar = windowInsetsCompat.getInsets(systemBar())
            outNavBar(sysBar)
            val bottom = if (!toNavEdge) navBar.bottom else 0
            val left = if (!toNavEdge) navBar.left else 0
            val right = if (!toNavEdge) navBar.right else 0
            val top = if (!toStatusEdge) statusBar.top else 0


            view.updatePadding(
                bottom = bottom,
                right = right,
                left = left,
                top = top
            )
        }
        activity.contentView.onWindowInsetsAnimationCallback(object :
            ViewInsetsAnimCall(DISPATCH_MODE_CONTINUE_ON_SUBTREE) {
            override fun onProgress(
                insets: WindowInsetsCompat,
                runningAnimations: MutableList<WindowInsetsAnimationCompat>
            ): WindowInsetsCompat {
                runningAnimations.forEach {
                    if (it.typeMask == type.type) {
                        val inset = insets.getInsets(it.typeMask)
                        val bottom = inset.bottom
                        animCallBack?.invoke(bottom)
                    }
                }
                return insets
            }
        })
    }

    private fun outNavBar(navBar: Insets) {
        OutBar.outNavBar(this, navBar)
    }


    override fun fullScreen(isAdapterBang: Boolean, isSticky: Boolean) {
        statusBar.hide(isAdapterBang)
        navBar.hide()
    }

    override fun unFullScreen() {
        statusBar.show()
        navBar.show()
    }

    override var edgeToEdge: Boolean
        get() = statusBar.toEdge && navBar.toEdge
        set(value) {
            statusBar.toEdge = value
            navBar.toEdge = value
        }


    override fun getStatusBar(): StatusBar {
        return statusBar
    }

    override fun getNavigationBar(): NavigationBar {
        return navBar
    }

    override fun animCallBack(type: TypeMask, callBack: (height: Int) -> Unit) {
        this.type = type
        animCallBack = callBack

    }

}