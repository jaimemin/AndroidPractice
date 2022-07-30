package com.tistory.jaimemin.androidpractice

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.w3c.dom.Text

class Intent_One : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent_one)

        // 암시적 인텐트
        // - 전화, SMS, Google Play Store, Website, GoogleMap, 사진첩, etc
        // - 상수
        //    - 변하지 않는 수 (문자)
        //    - 상수의 변수명은 전부 대문자로 만드는 문화
        // - URI (Uniform Resource Identifier)
        //    - id라고 생각을 하면 됨
        //    - 자원을 나타내는 고유한 주소
        //    - URL
        //        - 인터넷 페이지의 고유한 주소
        val implicitIntent : TextView = findViewById(R.id.implicit_intent)
        implicitIntent.setOnClickListener {
            val intent : Intent = Intent(Intent.ACTION_DIAL
                , Uri.parse("tel:" + "010-0000-0000"))

            startActivity(intent)
        }

        // 명시적 인텐트 + ComponentName -> 액티비티 전환
        val intentOne: TextView = findViewById(R.id.intent_one)
        intentOne.setOnClickListener {
            val intent: Intent = Intent()
            val componentName: ComponentName = ComponentName(
                "com.tistory.jaimemin.androidpractice",
                "com.tistory.jaimemin.androidpractice.Intent_Two"
            )

            intent.component = componentName
            startActivity(intent)
        }

        // 명시적 인텐트 -> 액티비티 전환
        // Context
        // 문맥
        // A 액티비티 -> B 액티비티
        (findViewById<TextView>(R.id.intent_two)).apply {
            this.setOnClickListener {
                startActivity(
                    Intent(this@Intent_One, Intent_Two::class.java)
                )
            }
        }

        // 명시적 인텐트 + data 전달
        (findViewById<TextView>(R.id.intent_three)).apply {
            this.setOnClickListener {
                val intent = Intent(this@Intent_One, Intent_Two::class.java)
                intent.putExtra("extra-data", "data-one")

                startActivity(intent)
            }
        }

        // 명시적 인텐트 + 결과 받기
        // requestCode
        // - 구분을 위해 존재
        // - Intent_One -> Intent_Two (request 1)
        // - Intent_One -> Intent_Three (request 2)
        // - Intent_One -> Intent_Four (request 3)
        (findViewById<TextView>(R.id.intent_four)).apply {
            this.setOnClickListener {
                val intent = Intent(this@Intent_One, Intent_Two::class.java)
                startActivityForResult(intent, 1) // deprecated
            }
        }

        // 명시적 인텐트 + 결과 받기 (ActivityResult API)
        // - requestCode가 존재하지 않는다
        // -> requestCode 없이도 요청자 구분 가능
        val startActivityLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                // onActivityResult에 해당하는 부분
                when(it.resultCode) {
                    RESULT_OK -> {
                        Log.d("INTENT_ONE data", it.data?.extras?.getString("result").toString())
                    }
                }

                // onAcitivityResult
                // - 모든 intent가 한 곳에서 처리 -> 구분 필요 (request code)
                // AcitivityResult API
                // - 각각의 intent가 처리되는 곳이 별도로 있다 -> 구분 필요 X
            }

        (findViewById<TextView>(R.id.intent_five)).apply {
            this.setOnClickListener {
                val intent = Intent(this@Intent_One, Intent_Two::class.java)
                startActivityLauncher.launch(intent)
            }
        }

        // 명시적 인텐트 + 이미지 URI 전달
        (findViewById<TextView>(R.id.intent_six)).apply {
            this.setOnClickListener {
                val intent = Intent(this@Intent_One, Intent_Two::class.java).apply {
                    val imageUri
                        = Uri.parse("android.resource://"
                            + packageName
                            + "/drawable/"
                            + "drawable_1")
                    this.action = Intent.ACTION_SEND
                    this.putExtra(Intent.EXTRA_STREAM, imageUri)
                    this.setType("image/*")
                }

                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // resultCode (status code)
        // - 최종 결과
        // - 성공, 실패
        when (requestCode) {
            1 -> {
                when (resultCode) {
                    RESULT_OK -> {
                        val data: String? = data?.extras?.getString("result")

                        Log.d("Intent_One data", data!!)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}