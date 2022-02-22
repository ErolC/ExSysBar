package com.erolc.exbar.navigationbar

interface NavigationBar {
    /**
     * 导航栏高度，如果调用了[hide]那么返回值将是0
     */
    val height: Int

    /**
     * 导航栏背景
     */
    var background: Int

    /**
     * 导航栏内容颜色是否为暗色
     */
    var colorIsDark: Boolean

    /**
     * 导航栏固有高度，无论是否调用了[hide]，都可以返回准确的高度
     */
    val inherentHeight: Int

    /**
     * 到边，表现为（导航栏背景透明，内容入侵到导航栏里，导航栏的内容覆盖在应用的内容上）
     */
    var toEdge: Boolean

    /**
     * 展示导航栏
     */
    fun show()

    /**
     * 隐藏导航栏
     */
    fun hide()

    /**
     * 是否隐藏导航栏
     */
    fun isHide():Boolean
}