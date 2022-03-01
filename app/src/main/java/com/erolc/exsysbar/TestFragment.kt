package com.erolc.exsysbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erolc.exbar.systemBar.statusBar

class TestFragment : Fragment() {
    val statusBar by statusBar {
        colorIsDark = true
        toEdge = true
    }
    companion object{
        fun newInstance(): TestFragment {
            val args = Bundle()

            val fragment = TestFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  LayoutInflater.from(context).inflate(R.layout.activity_main, container, false)

        return view
    }
}