package com.tistory.jaimemin.androidpractice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.myapplication.Post
import com.example.myapplication.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InstaFeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.insta_feed_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val feedListView = view.findViewById<RecyclerView>(R.id.feed_list)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.getAllPosts().enqueue(object : Callback<ArrayList<Post>> {
            override fun onResponse(
                call: Call<ArrayList<Post>>,
                response: Response<ArrayList<Post>>
            ) {
                if (!response.isSuccessful) {
                    return
                }

                val postList = response.body()
                val postRecyclerView = view.findViewById<RecyclerView>(R.id.feed_list)
                postRecyclerView.adapter = PostAdapter(
                    postList!!,
                    LayoutInflater.from(activity),
                    Glide.with(activity!!)
                )
            }

            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {

            }

        })
    }

    class PostAdapter(
        var postList: ArrayList<Post>,
        val inflater: LayoutInflater,
        val glide: RequestManager
    ) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val postOwner: TextView
            val postImage: ImageView
            val postContent: TextView

            init {
                postOwner = itemView.findViewById(R.id.post_owner)
                postImage = itemView.findViewById(R.id.post_img)
                postContent = itemView.findViewById(R.id.post_content)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = inflater.inflate(R.layout.post_item, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return postList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.postOwner.setText(postList.get(position).owner)
            holder.postContent.setText(postList.get(position).content)
            glide.load(postList.get(position).image).centerCrop().into(holder.postImage)
        }
    }
}