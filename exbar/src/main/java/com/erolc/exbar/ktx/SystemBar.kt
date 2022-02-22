package com.erolc.exbar.ktx

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.util.Pair
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.erolc.exbar.*
import com.erolc.exbar.model.TypeMask
import com.erolc.exbar.utils.*
import com.erolc.exbar.utils.KeyBoardHandler
import com.erolc.exbar.utils.ime

fun EditText.showIme() {
    insetsController?.show(ime())
}

fun EditText.hideIme() {
    insetsController?.hide(ime())
    clearFocus()
}

fun View.onSystemBarAnimCallBack(type:TypeMask =TypeMask.IME, callBack: (height: Int) -> Unit) {
    onWindowInsetsAnimationCallback(object : ViewInsetsAnimCall(DISPATCH_MODE_CONTINUE_ON_SUBTREE) {
        override fun onProgress(
            insets: WindowInsetsCompat,
            runningAnimations: MutableList<WindowInsetsAnimationCompat>
        ): WindowInsetsCompat {
            runningAnimations.forEach {
                if (it.typeMask == type.type) {
                    val bottom = insets.getInsets(it.typeMask).bottom
                    callBack.invoke(bottom)
                }
            }
            return insets
        }
    })
}

fun Activity.showIme() {
    insetsController?.show(ime())
}

fun Fragment.showIme() {
    activity?.insetsController?.show(ime())
}

fun Activity.hideIme() {
    insetsController?.hide(ime())
}

fun Fragment.hideIme() {
    activity?.insetsController?.hide(ime())
}

/**
 * view排除在SystemBar之外，布局也是正常布局就可以了，加上这一段代码那么该view将永远不会被statusBar/navigationBar所遮盖
 */
fun View.outEdge() {
    ExSystemBar.outEdgeViews.remove(this)
    ExSystemBar.outEdgeViews.add(this)
}

/**
 * 在自定义View的时候，在onDraw方法内时间该方法即可实现禁用手势区域
 * 最多只会禁用200dp的区域（是所有的综合起来的），当超过这个区域之后，系统是不会兑现的。
 */
fun View.setGestureExclusion() {
    val r = Rect()
    getLocalVisibleRect(r)
    ViewCompat.setSystemGestureExclusionRects(this, listOf(r))
}

/**
 * 调用该方法可直接禁用部分手势区域
 */
fun View.applyGestureExclusion() {
    applyWindowInsetsListener { view, _ ->
        view.setGestureExclusion()
    }
}

/**
 * 使用该方法重写activity的dispatchTouchEvent回调，即可实现点击任意位置收起软键盘
 * @param superDispatchTouchEvent 是一个方法，该方法中请调用super.dispatchTouchEvent(ev)方法
 * @param noHideKeyboardViews 排除这些view，使得点击这些view并不会收起软键盘
 * @param ev dispatchTouchEvent的ev
 *
 * eg：
 *   override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
 *      return hideImeWithOutViews({ super.dispatchTouchEvent(it) }, listOf(), ev)
 *   }
 */
fun Activity.hideImeWithOutViews(
    superDispatchTouchEvent: (MotionEvent?) -> Boolean,
    noHideKeyboardViews: List<View>,
    ev: MotionEvent?
): Boolean =
    KeyBoardHandler.dispatchTouchEvent(this, superDispatchTouchEvent, noHideKeyboardViews, ev)


internal fun View.getPosition(): Pair<Int, Int> {
    val position = IntArray(2)
    getLocationInWindow(position)
    return Pair(position[0], position[1])
}

internal val screenHeight: Int get() = Resources.getSystem().displayMetrics.heightPixels

val Context.statusBarHeight: Int
    get() {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }


