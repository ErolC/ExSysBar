package com.erolc.exbar.statusbar

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import androidx.core.graphics.ColorUtils
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentActivity
import com.erolc.exbar.*
import com.erolc.exbar.systemBar.SystemBarImpl
import com.erolc.exbar.utils.DisplayCutoutHandler
import com.erolc.exbar.utils.contentView
import com.erolc.exbar.utils.getWindowInsetsController
import com.erolc.exbar.utils.statusBar

class StatusBarImpl(private val activity: FragmentActivity) : StatusBar {
    val controller by lazy { activity.getWindowInsetsController() }

    var systemBarImpl: SystemBarImpl? = null

    private var isHide = false
    private var _background:Int = background

    private fun Activity.getHeight(): Int {
        val identifier =
            resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (identifier > 0) resources.getDimensionPixelSize(identifier) else 0
    }

    private var hasNotchInScreen: Boolean? = null
    private var isAdapterBang: Boolean? = null

    init {
        DisplayCutoutHandler.hasNotchInScreen {
            hasNotchInScreen = it
            if (isAdapterBang != null) {
                hide(isAdapterBang == true)
                isAdapterBang = null
            }
        }
    }


    /**
     * 标题栏高度
     */
    private val Activity.actionBarHeight
        get() = run {
            val hasActionBar =
                window.decorView.findViewById<View>(R.id.action_mode_bar_stub) == null//这个view在标题栏存在时，不存在
            val value = TypedValue()
            if (theme.resolveAttribute(android.R.attr.actionBarSize, value, true) && hasActionBar)
                TypedValue.complexToDimensionPixelSize(value.data, resources.displayMetrics)
            else
                0
        }


    /**
     * 检查颜色是否为亮色
     */
    private fun isLightColor(color: Int) = ColorUtils.calculateLuminance(color) >= 0.5


    override val height: Int get() = if (isHide) { 0 } else { inherentHeight }

    override val inherentHeight: Int get() = activity.getHeight()

    override fun isHide() = isHide

    override var background: Int
        get() = activity.window.statusBarColor
        set(value) {
            activity.window.statusBarColor = value
            colorIsDark = isLightColor(value)
        }

    override var colorIsDark: Boolean
        get() = controller?.isAppearanceLightStatusBars ?: false
        set(value) {
            controller?.isAppearanceLightStatusBars = value
        }


    override var toEdge: Boolean
        get() = systemBarImpl?.isStatusVisible ?: false
        set(value) {
            systemBarImpl?.isStatusVisible = value
            if (!value) {
                _background = background
                background = Color.TRANSPARENT
            }else{
                background = _background
            }
            activity.contentView.requestApplyInsets()
        }

    override fun show() {
        isHide = false
        controller?.show(statusBar())
    }

    override fun hide(isAdapterBang: Boolean) {
        isHide = true
        controller?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
        controller?.hide(statusBar())

        //一个保险机制，当检测是否存在刘海比该方法晚时，需要将isAdapterBang存起来，在检测是否存在刘海完成时再次调用该方法
        if (hasNotchInScreen == null) {
            this.isAdapterBang = isAdapterBang
            return
        }

        if (hasNotchInScreen == true) {
            adapterBang(isAdapterBang)
        } else {
            adapterBang(true)
        }
    }


    fun setSystemBar(systemBarImpl: SystemBarImpl) {
        this.systemBarImpl = systemBarImpl
    }

    private fun adapterBang(isAdapterBang: Boolean = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lp: WindowManager.LayoutParams = activity.window.attributes
            lp.layoutInDisplayCutoutMode = if (isAdapterBang) {
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            } else {
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
            }

            activity.window.attributes = lp
        }
    }

}