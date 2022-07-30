package com.tistory.jaimemin.androidpractice

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView

class Intent_Two : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent_two)

        // Intent_One -> Intent_Two
        val intent = intent
        val data: String? = intent.extras?.getString("extra-data")

        if (data != null) {
            Log.d("Intent_Two data", data)
        }

        (findViewById<TextView>(R.id.finish)).apply {
            this.setOnClickListener {
                val intent : Intent = Intent()
                intent.putExtra("result", "success")
                setResult(RESULT_OK, intent)

                finish()
            }
        }

        val imageView = findViewById<ImageView>(R.id.imageView)
        val imageUri = Uri.parse(
            intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM).toString()
        )
        imageView.setImageURI(imageUri)
    }
}