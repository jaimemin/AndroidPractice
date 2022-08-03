package com.tistory.jaimemin.androidpractice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class FragmentFirst: Fragment() {

    // inflater: XML을 화면에 사용할 준비를 하는 객체 (XML -> View로 전환)
    // container: fragment에서 사용될 XML의 부모 뷰
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // attachToRoot: 루트 View에 무조건 붙는데 언제 붙냐의 차이 (true/false)
        val view = inflater.inflate(R.layout.first_fragment, container, false)
        view.findViewById<TextView>(R.id.call_activity).setOnClickListener {
            (activity as FragmentActivity).printTestLog()
        }

        return view;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data : String? = arguments?.getString("key")
        Log.d("bundle arguments", "data is " + data)
    }

    fun printTestLog() {
        Log.d("testt", "print test log from fragment first")
    }
}