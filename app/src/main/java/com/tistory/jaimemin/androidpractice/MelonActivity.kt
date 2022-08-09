package com.tistory.jaimemin.androidpractice

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.myapplication.RetrofitService
import com.example.myapplication.Song
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable

class MelonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_melon)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.getSongList().enqueue(object : Callback<ArrayList<Song>> {
            override fun onResponse(
                call: Call<ArrayList<Song>>,
                response: Response<ArrayList<Song>>
            ) {
                if (!response.isSuccessful) {
                    return;
                }

                val songList = response.body()

                findViewById<RecyclerView>(R.id.melon_list_view).apply {
                    this.adapter = MelonItemRecyclerAdapter (
                        songList!!,
                        LayoutInflater.from(this@MelonActivity),
                        Glide.with(this@MelonActivity),
                        this@MelonActivity
                    )
                }
            }

            override fun onFailure(call: Call<ArrayList<Song>>, t: Throwable) {
                Log.d("melon", "요청 실패 " + t.message)
            }

        })
    }
}

class MelonItemRecyclerAdapter(
    val songList : ArrayList<Song>,
    val inflater : LayoutInflater,
    val glide : RequestManager,
    val context : Context
) : RecyclerView.Adapter<MelonItemRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView
        val thumbnail : ImageView
        val play : ImageView

        init {
            title = itemView.findViewById(R.id.title)
            thumbnail = itemView.findViewById(R.id.thumbnail)
            play = itemView.findViewById(R.id.play)

            play.setOnClickListener {
                val intent = Intent(context, MelonDetailActivity::class.java)
                intent.putExtra("song_list", songList as Serializable)
                intent.putExtra("position", adapterPosition)

                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            inflater.inflate(R.layout.melon_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = songList.get(position).title
        glide.load(songList.get(position).thumbnail).centerCrop().into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return songList.size
    }
}