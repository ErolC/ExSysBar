package com.erolc.exbar.systemBar

import androidx.core.view.WindowInsetsCompat
import com.erolc.exbar.navigationbar.NavigationBar
import com.erolc.exbar.statusbar.StatusBar
import com.erolc.exbar.utils.ime
import androidx.core.view.WindowInsetsCompat.Type.InsetsType
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

    fun getStatusBar(): StatusBar
    fun getNavigationBar(): NavigationBar

    fun animCallBack(type:TypeMask = TypeMask.IME, callBack: (height: Int) -> Unit)
}