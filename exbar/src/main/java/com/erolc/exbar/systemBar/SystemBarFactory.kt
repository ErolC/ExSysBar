package com.erolc.exbar.systemBar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.erolc.exbar.navigationbar.LifeCycleNavBar
import com.erolc.exbar.navigationbar.NavigationBarImpl
import com.erolc.exbar.statusbar.LifeCycleStatusBar
import com.erolc.exbar.statusbar.StatusBarImpl

/**
 * StatusBar的对象工厂
 * 管理StatusBar对象
 */
internal class SystemBarFactory private constructor() {
    //以hashCode为key，保证唯一
    private val map: MutableMap<Int, SystemBar> = mutableMapOf()

    companion object {
        /**
         * 得到一个工厂对象
         */
        private val factory: SystemBarFactory by lazy { SystemBarFactory() }

        /**
         * 创建一个生命周期感知的statusBar对象
         * @param activity activity
         */
        fun create(activity: FragmentActivity): SystemBar {
            val key = activity.hashCode()
            var systemBar = factory.map[key]
            if (systemBar == null) {
                val realStatusBar = StatusBarImpl(activity)
                val realNavBar = NavigationBarImpl(activity)

                synchronized(SystemBarImpl::class) {
                    val statusBar = LifeCycleStatusBar(activity, realStatusBar)
                    val navBar = LifeCycleNavBar(activity, realNavBar)
                    systemBar = SystemBarImpl(activity, navBar, statusBar)
                    factory.map[key] = systemBar!!
                }
            }

            return systemBar!!
        }

        /**
         * 创建一个生命周期感知的statusBar对象
         */
        fun create(fragment: Fragment): SystemBar {
            val key = fragment.hashCode()
            var systemBar = factory.map[key]
            if (systemBar == null) {
                //如果这里获取不到activity，那么证明当前的fragment不适合操作statusbar
                val activitySystemBar = create(fragment.requireActivity()) as SystemBarImpl
                synchronized(SystemBarImpl::class) {
                    val statusBar = LifeCycleStatusBar(
                        fragment,
                        activitySystemBar.getStatusBar()
                    )
                    val navBar = LifeCycleNavBar(
                        fragment,
                        activitySystemBar.getNavigationBar()
                    )
                    systemBar = SystemBarImpl(fragment.requireActivity(), navBar, statusBar)
                    factory.map[key] = systemBar!!
                }

            }
            return systemBar!!
        }

        internal fun create(owner: LifecycleOwner): SystemBar {
            return if (owner is Fragment) {
                create(owner)
            } else {
                create(owner as FragmentActivity)
            }
        }

        /**
         * 清除特定的状态栏对象
         */
        fun clear(hashCode: Int) {
            factory.map.remove(hashCode)
        }
    }
}
