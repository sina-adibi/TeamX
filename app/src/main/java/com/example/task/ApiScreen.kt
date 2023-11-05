package com.example.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task.api.ApiService
import com.example.task.api.PostAdapter
import com.example.task.api.ServiceGenerator
import com.example.task.room.PostDao
import com.example.task.room.PostDatabase
import com.example.task.room.PostEntity
import com.example.task.room.PostViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiScreen : AppCompatActivity() {
    private lateinit var postViewModel: PostViewModel
    private lateinit var postDao: PostDao
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_screen)

        postDao = PostDatabase.getInstance(this).postDao()
        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        recyclerView = findViewById(R.id.myRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.getPosts()

        call.enqueue(object : Callback<List<PostEntity>> {
            override fun onResponse(call: Call<List<PostEntity>>, response: Response<List<PostEntity>>) {
                if (response.isSuccessful) {
                    val posts = response.body()
                    posts?.let { posts ->
                        GlobalScope.launch {
                            for (post in posts) {
                                postViewModel.insertData(postDao, post.id, post.saveDate, post.message, post.seen)
                            }
                        }
                    }
                    recyclerView.adapter = PostAdapter(posts ?: emptyList())
                } else {
                }
            }

            override fun onFailure(call: Call<List<PostEntity>>, t: Throwable) {
            }
        })
    }
}

