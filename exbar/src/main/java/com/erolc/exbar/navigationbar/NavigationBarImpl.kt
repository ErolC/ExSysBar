package com.erolc.exbar.navigationbar

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentActivity
import com.erolc.exbar.systemBar.SystemBarImpl
import com.erolc.exbar.utils.contentView
import com.erolc.exbar.utils.insetsController
import com.erolc.exbar.utils.navigationBar

class NavigationBarImpl(private val activity: FragmentActivity) : NavigationBar {
    private val controller by lazy { activity.insetsController }

    private var systemBarImpl: SystemBarImpl? = null
    private var isHide = false
    private var _background = background

    /**
     * 检查颜色是否为亮色
     */
    private fun isLightColor(color: Int) = ColorUtils.calculateLuminance(color) >= 0.5


    private val defHeight: Int
        get() {
            val identifier =
                activity.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            return if (identifier > 0) activity.resources.getDimensionPixelSize(identifier) else 0
        }


    override val height: Int get() = if (isHide) 0 else inherentHeight

    override val inherentHeight: Int get() = defHeight

    override fun isHide() = isHide

    override var background: Int
        get() = activity.window.navigationBarColor
        set(value) {
            activity.window.navigationBarColor = value
            colorIsDark = isLightColor(value)
        }

    override var colorIsDark: Boolean
        get() = controller?.isAppearanceLightNavigationBars ?: false
        set(value) {
            controller?.isAppearanceLightNavigationBars = value
        }

    override var toEdge: Boolean
        get() = systemBarImpl?.isNavVisible ?: false
        set(value) {
            systemBarImpl?.isNavVisible = value
            if (!value) {
                _background = background
                background = Color.TRANSPARENT
            } else {
                background = _background
            }
            activity.contentView.requestApplyInsets()
        }

    override fun show() {
        isHide = false
        controller?.show(navigationBar())
    }

    override fun hide() {
        isHide = true
        controller?.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller?.hide(navigationBar())
    }


    fun setSystemBar(systemBarImpl: SystemBarImpl) {
        this.systemBarImpl = systemBarImpl
    }

}