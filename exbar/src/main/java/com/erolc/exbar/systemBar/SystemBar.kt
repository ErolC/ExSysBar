package com.erolc.exbar.systemBar

import com.erolc.exbar.navigationbar.NavigationBar
import com.erolc.exbar.statusbar.StatusBar
import com.erolc.exbar.model.TypeMask

interface SystemBar {
    /**
     * 全屏
     * @param isAdapterBang 是否适配刘海
     * @param isSticky 是否是粘性,粘性模式下，应用将占用手势区域，将无法直接使用左右滑动进行后退，需要先从下方向上滑动，呼出底部导航栏之后才可触发手势区域
     */
    fun fullScreen(isAdapterBang: Boolean = true, isSticky: Boolean = true)

    /**
     * 解除全屏
     */
    fun unFullScreen()

    /**
     * 边到边，保留状态栏和导航栏，但是背景会变透明，并且内容会侵入到两个系统栏中。使用[com.erolc.exbar.ktx.outEdge]方法可以避免view被状态栏或导航栏遮挡
     */
    var edgeToEdge:Boolean

    /**
     * 获得状态栏对象
     */
    fun getStatusBar(): StatusBar

    /**
     * 获得导航栏对象
     */
    fun getNavigationBar(): NavigationBar

    /**
     * 栏变化的动画过程回调
     * 也可以使用[com.erolc.exbar.ktx.onSystemBarAnimCallBack]监听这一变化
     * @param type 指定栏的类型，比如[TypeMask.StatusBar]是状态栏，详细请查看[TypeMask]类
     * @param callBack 是动画过程中栏的高度变化。
     */
    fun animCallBack(type:TypeMask = TypeMask.IME, callBack: (height: Int) -> Unit)
}