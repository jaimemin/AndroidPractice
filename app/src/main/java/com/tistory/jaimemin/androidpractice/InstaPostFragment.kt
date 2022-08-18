package com.tistory.jaimemin.androidpractice

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.Post
import com.example.myapplication.RetrofitService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class InstaPostFragment : Fragment() {

    var imageUri : Uri? = null

    var contentInput : String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.insta_post_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedImageView = view.findViewById<ImageView>(R.id.selected_img)
        val glide = Glide.with(activity as InstaMainActivity)

        val imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                imageUri = it.data!!.data

                glide.load(imageUri).into(selectedImageView)
            }
        imagePickerLauncher.launch(Intent(Intent.ACTION_PICK).apply {
            this.type = MediaStore.Images.Media.CONTENT_TYPE
        })

        view.findViewById<EditText>(R.id.selected_content).doAfterTextChanged {
            contentInput = it.toString()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitService = retrofit.create(RetrofitService::class.java)

        view.findViewById<TextView>(R.id.upload).setOnClickListener {
            val file = getRealFile(imageUri!!)
            val requestFile = RequestBody
                .create(MediaType.parse((activity as InstaMainActivity).contentResolver.getType(imageUri!!)), file)
            val content = RequestBody.create(MultipartBody.FORM, contentInput)
            val body = MultipartBody.Part.createFormData("image", file!!.name, requestFile)
//            val header = HashMap<String, String>()
//            val sharedPreference = (activity as InstaMainActivity).getSharedPreferences(
//                "user_info"
//                , Context.MODE_PRIVATE
//            )
//            val token = sharedPreference.getString("token", "")
//            header.put("Authorization", token!!)

            retrofitService.uploadPost(body, content).enqueue(object: Callback<Post>{

                override fun onResponse(call: Call<Post>, response: Response<Post>) {

                }

                override fun onFailure(call: Call<Post>, t: Throwable) {

                }

            });
        }
    }

    private fun getRealFile(uri: Uri) : File? {
        var uri : Uri? = uri
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        var cursor: Cursor? = (activity as InstaMainActivity).getContentResolver().query(
            uri!!,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_MODIFIED + " desc"
        )

        if (cursor == null || cursor.columnCount < 1) {
            return null
        }

        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()

        val path : String = cursor.getString(column_index)

        if (cursor != null) {
            cursor.close()

            cursor = null
        }

        return File(path)
    }
}