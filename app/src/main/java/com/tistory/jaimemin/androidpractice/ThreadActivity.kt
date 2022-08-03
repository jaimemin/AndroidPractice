package com.tistory.jaimemin.androidpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class ThreadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread)

        val currentThread = Thread.currentThread()
        Log.d("testt", "" + currentThread)

//        Thread {
//            for (a in 1..1000) {
//                Log.d("testt", "A" + a)
//            }
//        }.start()
//
//        Thread {
//            for (a in 1..1000) {
//                Log.d("testt", "B" + a)
//            }
//        }.start()

        Thread {
            val currentThread = Thread.currentThread()
            Log.d("testt", "A" + currentThread)

            runOnUiThread {
                findViewById<TextView>(R.id.test).text = "changed"
            }
        }.start()
    }
}