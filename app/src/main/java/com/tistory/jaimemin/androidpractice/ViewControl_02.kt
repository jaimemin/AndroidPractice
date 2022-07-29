package com.tistory.jaimemin.androidpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlin.math.log

class ViewControl_02 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_control02)

        // view를 activity로 가져오는 방법
        val textViewOne : TextView = findViewById(R.id.textViewOne)
        val buttonOne : Button = findViewById(R.id.buttonOne)

        Log.d("textViewOne's text", textViewOne.text as String)

        // Listener 사용 방법 (람다 버전)
        buttonOne.setOnClickListener {
            Log.d("buttonOne click", "버튼 클릭!!")
        }

//        val clickListener = object : View.OnClickListener {
//            override fun onClick(p0: View?) {
//                Log.d("buttonOne click", "버튼 클릭!!")
//            }
//        }
//        buttonOne.setOnClickListener(clickListener)

        // 익명 함수
//        buttonOne.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(p0: View?) {
//                Log.d("buttonOne click", "버튼 클릭!!")
//            }
//        })
    }
}