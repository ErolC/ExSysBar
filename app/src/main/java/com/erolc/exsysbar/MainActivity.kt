package com.erolc.exsysbar

import android.app.Activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.erolc.exbar.ktx.hideImeWithOutViews
import com.erolc.exbar.ktx.onSystemBarAnimCallBack
import com.erolc.exbar.ktx.outEdge
import com.erolc.exbar.ktx.showIme
import com.erolc.exbar.systemBar.navigationBar
import com.erolc.exbar.systemBar.statusBar


class MainActivity : AppCompatActivity() {

    val systemBar by statusBar {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<View>(R.id.hide)
        val view1 = findViewById<View>(R.id.text)

        view.outEdge()
        view1.outEdge()
        view.onSystemBarAnimCallBack() {
            Log.e("TAG", "onCreate: $it" )
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return hideImeWithOutViews({ super.dispatchTouchEvent(it) }, listOf(), ev)
    }

    fun next(view: View) {
//        val intent = Intent(this, NavTestActivity::class.java)
//        startActivity(intent)
        showIme()
    }

    fun hide(view: View) {
        systemBar.hide()

    }

    fun show(view: View) {
        systemBar.show()
    }

    fun showRedColor(view: View) {
        systemBar.background = randomColor()
    }

    fun getStatusBarHeight(view: View) {
        showToast(systemBar.height)
    }

    fun switchTextColor(view: View) {
        val textColorIsDark = systemBar.colorIsDark
        systemBar.colorIsDark = !textColorIsDark
    }

    fun immersive(view: View) {
        val invasion = systemBar.toEdge
        systemBar.toEdge = !invasion
    }

    fun randomColor(): Int {
        val r = Math.random() * 255
        val g = Math.random() * 255
        val b = Math.random() * 255
        return Color.rgb(r.toInt(), g.toInt(), b.toInt())
    }


    fun <T> Activity.showToast(t: T) {
        Toast.makeText(this, "$t", Toast.LENGTH_SHORT).show()
    }
}