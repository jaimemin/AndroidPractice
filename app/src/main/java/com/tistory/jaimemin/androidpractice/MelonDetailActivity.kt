package com.tistory.jaimemin.androidpractice

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myapplication.Song

class MelonDetailActivity : AppCompatActivity() {

    lateinit var songList: ArrayList<Song>

    lateinit var playPauseButton : ImageView

    lateinit var mediaPlayer : MediaPlayer

    var position = 0

    var isPlaying : Boolean = true
        set (value) {
            if (value == true) {
                playPauseButton.setImageDrawable(
                    this.resources.getDrawable(R.drawable.pause, this.theme)
                )
            } else {
                playPauseButton.setImageDrawable(
                    this.resources.getDrawable(R.drawable.play, this.theme)
                )
            }

            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_melon_detail)

        songList = intent.getSerializableExtra("song_list") as ArrayList<Song>
        position = intent.getIntExtra("position", 0)
        playSongItem(songList.get(position))
        changeThumbnail(songList.get(position))

        playPauseButton = findViewById(R.id.play)
        playPauseButton.setOnClickListener {
            if (isPlaying) {
                mediaPlayer.stop()
            } else {
                mediaPlayer.start()
                playSongItem(songList.get(position))
            }

            isPlaying = !isPlaying
        }

        findViewById<ImageView>(R.id.back).setOnClickListener {
            mediaPlayer.stop()

            position = (position - 1 + songList.size) % songList.size
            playSongItem(songList.get(position))
            changeThumbnail(songList.get(position))
        }

        findViewById<ImageView>(R.id.next).setOnClickListener {
            mediaPlayer.stop()

            position = (position + 1) % songList.size
            playSongItem(songList.get(position))
            changeThumbnail(songList.get(position))
        }
    }

    fun playSongItem(songItem : Song) {
        mediaPlayer = MediaPlayer.create(
            this,
            Uri.parse(songItem.song)
        )
        mediaPlayer.start()
    }

    fun changeThumbnail(songItem : Song) {
        findViewById<ImageView>(R.id.thumbnail).apply {
            val glide = Glide.with(this@MelonDetailActivity)
            glide.load(songItem.thumbnail).into(this)
        }
    }
}