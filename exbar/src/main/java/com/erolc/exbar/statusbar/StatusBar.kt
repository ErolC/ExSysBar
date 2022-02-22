package com.erolc.exbar.statusbar

interface StatusBar {
    /**
     * 状态栏高度，如果调用了[hide]那么返回值将是0
     */
    val height: Int

    /**
     * 状态栏背景
     */
    var background: Int

    /**
     * 状态栏内容颜色是否为暗色
     */
    var colorIsDark: Boolean

    /**
     * 状态栏固有高度，无论是否调用了[hide]，都可以返回准确的高度
     */
    val inherentHeight: Int

    /**
     * 到边，表现为（状态栏背景透明，内容入侵到状态栏里，状态栏的内容覆盖在应用的内容上）
     */
    var toEdge: Boolean

    /**
     * 展示状态栏
     */
    fun show()

    /**
     * 隐藏状态栏
     * @param isAdapterBang 是否适配刘海
     */
    fun hide(isAdapterBang:Boolean = true)

    /**
     * 是否隐藏状态栏
     */
    fun isHide():Boolean
}