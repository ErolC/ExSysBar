package com.erolc.exbar.model

import com.erolc.exbar.utils.*
import com.erolc.exbar.utils.ime
import com.erolc.exbar.utils.navigationBar
import com.erolc.exbar.utils.statusBar
import com.erolc.exbar.utils.systemBar

sealed class TypeMask(val type:Int) {
    object StatusBar:TypeMask(statusBar())
    object NavigationBar:TypeMask(navigationBar())
    object IME:TypeMask(ime())
    object SystemBar:TypeMask(systemBar())
    object CaptionBar:TypeMask(captionBar())
}