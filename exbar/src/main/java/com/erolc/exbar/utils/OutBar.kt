package com.erolc.exbar.utils

import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.util.component1
import androidx.core.util.component2
import androidx.core.view.updateLayoutParams
import com.erolc.exbar.ExSystemBar
import com.erolc.exbar.systemBar.SystemBarImpl
import com.erolc.exbar.model.ViewSize

object OutBar {

    private val defSize = mutableMapOf<Int, ViewSize>()

    fun outNavBar(systemBarImpl: SystemBarImpl, systemBar: Insets) {
        val bottom = if (systemBarImpl.toNavEdge && systemBarImpl.isNavVisible) systemBar.bottom else 0
        val left = if (systemBarImpl.toNavEdge && systemBarImpl.isNavVisible) systemBar.left else 0
        val right = if (systemBarImpl.toNavEdge && systemBarImpl.isNavVisible) systemBar.right else 0
        val top = if (systemBarImpl.toStatusEdge && systemBarImpl.isStatusVisible) systemBar.top else 0
        ExSystemBar.outEdgeViews.forEach {
            it.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                var size = defSize[it.hashCode()]
                if (size == null) {
                    defSize[it.hashCode()] =
                        ViewSize(leftMargin, topMargin, rightMargin, bottomMargin)
                    size = defSize[it.hashCode()]
                }
                leftMargin = left + (size?.left ?: 0)
                rightMargin = right + (size?.right ?: 0)

                val (_, y) = it.getPosition()
                val center = screenHeight / 2
                if (y > center) {
                    bottomMargin = bottom + (size?.bottom ?: 0)
                } else {
                    topMargin = top + (size?.top ?: 0)
                }

            }
        }
    }
}