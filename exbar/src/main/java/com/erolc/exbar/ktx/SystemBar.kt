package com.erolc.exbar.ktx

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
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

fun View.hideIme() {
    insetsController?.hide(ime())
    clearFocus()
}

fun Activity.showIme() {
    insetsController?.show(ime())
}

fun Fragment.showIme() {
    insetsController?.show(ime())
}

fun Activity.hideIme() {
    insetsController?.hide(ime())
}

fun Fragment.hideIme() {
    insetsController?.hide(ime())
}


/**
 * view排除在SystemBar之外，布局也是正常布局就可以了，加上这一段代码那么该view将永远不会被statusBar/navigationBar所遮盖
 */
fun View.outEdge() {
    ExSystemBar.outEdgeViews.remove(this)
    ExSystemBar.outEdgeViews.add(this)
}

/**
 * 系统栏动画回调。
 */
fun View.onSystemBarAnimCallBack(
    type: TypeMask = TypeMask.IME,
    callBack: View.(height: Int) -> Unit
) {
    onWindowInsetsAnimationCallback(object : ViewInsetsAnimCall(DISPATCH_MODE_CONTINUE_ON_SUBTREE) {
        override fun onProgress(
            insets: WindowInsetsCompat,
            runningAnimations: MutableList<WindowInsetsAnimationCompat>
        ): WindowInsetsCompat {
            runningAnimations.forEach {
                if (it.typeMask == type.type) {
                    val bottom = insets.getInsets(it.typeMask).bottom
                    callBack.invoke(getView(), bottom)
                }
            }
            return insets
        }
    })
}

private fun View.updateGestureExclusion() {
    val gestureExclusionRects = mutableListOf<Rect>()
    // Skip this call if we're not running on Android 10+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val rootWindowInsets = rootWindowInsets ?: return

        val gestureInsets = WindowInsetsCompat.toWindowInsetsCompat(rootWindowInsets, this)
            .getInsets(WindowInsetsCompat.Type.systemGestures())

        gestureExclusionRects.clear()
        // Add an exclusion rect for the left gesture edge
        gestureExclusionRects += Rect(0, 0, gestureInsets.left, height)
        // Add an exclusion rect for the right gesture edge
        gestureExclusionRects += Rect(width - gestureInsets.right, 0, width, height)

        ViewCompat.setSystemGestureExclusionRects(this, gestureExclusionRects)
    }
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


fun View.applyWindowInsetsListener(body: ((View, WindowInsetsCompat) -> Unit)? = null) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        body?.invoke(v, insets)
        insets
    }
}


fun View.onWindowInsetsAnimationCallback(callBack: ViewInsetsAnimCall) {
    callBack.view = this
    ViewCompat.setWindowInsetsAnimationCallback(this, callBack)
}


abstract class ViewInsetsAnimCall(@DispatchMode dispatchMode: Int) :
    WindowInsetsAnimationCompat.Callback(dispatchMode) {
    internal var view: View? = null
    fun getView() = view!!
}

val Context.statusBarHeight: Int
    get() {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }


