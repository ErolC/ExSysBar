package com.erolc.exbar.utils

import android.app.Activity
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import com.erolc.exbar.ktx.hideIme

internal object KeyBoardHandler {
    @Volatile
    var removeFocus: Boolean = true

    @Volatile
    var removeKeyBoard = true

    internal fun dispatchTouchEvent(
        activity: Activity,
        superDispatchTouchEvent: (MotionEvent?) -> Boolean,
        noHideKeyboardViews: List<View>,
        ev: MotionEvent?
    ): Boolean {
        if (ev == null) {
            return superDispatchTouchEvent(ev)
        }
        if (ev.action == MotionEvent.ACTION_UP
            || ev.action == MotionEvent.ACTION_POINTER_UP
        ) {
            val v = activity.currentFocus
            if (v == null) {
                activity.hideIme()
                return superDispatchTouchEvent.invoke(ev)
            }
            if (isShouldHideInput(v, ev) && canHideKeyBoard(noHideKeyboardViews, ev)) {
                v.postDelayed({
                    if (removeFocus && removeKeyBoard) {
                        v.clearFocus()
                    } else {
                        removeFocus = true
                    }
                    if (removeKeyBoard) {
                        activity.hideIme()
                    } else {
                        removeKeyBoard = true
                    }
                }, 100)
            }
            return superDispatchTouchEvent.invoke(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return activity.window.superDispatchTouchEvent(ev) || activity.onTouchEvent(ev)
    }


    private fun canHideKeyBoard(noHideKeyboardViews: List<View>, ev: MotionEvent): Boolean {
        val map = noHideKeyboardViews.map {
            isShouldHideInputView(it, ev)
        }
        var result = true
        map.forEach {
            if (!it) {
                result = false
                return@forEach
            }
        }
        return result
    }


    /**
     * 通过点击区域，判断是否隐藏软键盘
     */
    private fun isShouldHideInput(v: View, event: MotionEvent): Boolean {
        if (v is EditText) {
            return isShouldHideInputView(v, event)
        }
        return false
    }

    /**
     * 通过点击区域，判断是否隐藏EditText
     */
    private fun isShouldHideInputView(v: View, event: MotionEvent): Boolean {
        val leftTop = intArrayOf(0, 0)
        // 获取输入框当前的location位置
        v.getLocationInWindow(leftTop)
        val left = leftTop[0]
        val top = leftTop[1]
        val bottom = top + v.height
        val right = left + v.width
        return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
    }
}