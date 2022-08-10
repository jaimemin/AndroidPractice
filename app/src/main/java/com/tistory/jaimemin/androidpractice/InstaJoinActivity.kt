package com.tistory.jaimemin.androidpractice

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.example.myapplication.RetrofitService
import com.example.myapplication.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InstaJoinActivity : AppCompatActivity() {

    var username : String = ""
    var password : String = ""
    var password2 : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insta_join)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitService = retrofit.create(RetrofitService::class.java)

        findViewById<TextView>(R.id.insta_login).setOnClickListener {
            startActivity(Intent(this, InstaLoginActivity::class.java))
        }

        findViewById<EditText>(R.id.id_input).doAfterTextChanged {
            username = it.toString()
        }

        findViewById<EditText>(R.id.pw_input).doAfterTextChanged {
            password = it.toString()
        }

        findViewById<EditText>(R.id.pw_input2).doAfterTextChanged {
            password2 = it.toString()
        }

        findViewById<TextView>(R.id.join_btn).setOnClickListener {
            retrofitService.register(username, password, password2).enqueue(object: Callback<User>{

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (!response.isSuccessful) {
                        return
                    }

                    val user : User = response.body()!!
                    user.token?.let { it1 -> Log.d("insta", it1) }

                    if (user.token == null) {
                        return
                    }

                    val sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE)
                    val editor : SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("token", user.token)
                    editor.putString("id", user.id)
                    editor.commit()

                    startActivity(Intent(this@InstaJoinActivity, InstaMainActivity::class.java))
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("insta", "insta join fail " + t.message)
                }

            })
        }
    }
}