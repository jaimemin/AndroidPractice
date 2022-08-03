package com.tistory.jaimemin.androidpractice

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView

class AsyncActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async)

        val backgroundTask = BackgroundAsyncTask(
            findViewById(R.id.progressBar),
            findViewById(R.id.progressBarText)
        )

        findViewById<TextView>(R.id.start).setOnClickListener {
            backgroundTask.execute()
        }

        findViewById<TextView>(R.id.stop).setOnClickListener {
            backgroundTask.cancel(true)
        }
    }
}

class BackgroundAsyncTask (
    val progressBar: ProgressBar,
    val progressText: TextView
): AsyncTask<Int, Int, Int>() {
    // Params, Progress, Result
    // Params -> doInBackground에 사용할 타입
    // Progress -> onProgressUpdate에서 사용할 타입
    // Result -> onPostExecute에서 사용할타입

    // deprecated -> 더 이상 사용을 권장 X
    // 코루틴 -> 멀티 태스킹, 동기/비동기 처리
    var percent: Int = 0

    override fun doInBackground(vararg p0: Int?): Int {
        while (isCancelled() == false) {
            percent++

            if (percent > 100) {
                break
            } else {
                publishProgress(percent)
            }

            Thread.sleep(100)
        }

        return percent
    }

    override fun onPreExecute() {
        percent = 0
        progressBar.setProgress(percent)
    }

    override fun onPostExecute(result: Int?) {
        progressText.text = "작업이 완료되었습니다."
    }

    override fun onProgressUpdate(vararg values: Int?) {
        progressBar.setProgress(values[0] ?: 0)
        progressText.text = "퍼센트: " + values[0]
    }

    override fun onCancelled() {
        progressBar.setProgress(0)
        progressText.text = "작업이 취소되었습니다."
    }
}