package com.example.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

import androidx.lifecycle.Observer
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
import com.example.task.utils.MessageInsertCallback
import com.example.task.utils.openDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiScreen : AppCompatActivity(), MessageInsertCallback {
    private lateinit var postViewModel: PostViewModel
    private lateinit var postDao: PostDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter

    var isApiCalled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_screen)

        postDao = PostDatabase.getInstance(this).postDao()
        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        recyclerView = findViewById(R.id.myRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (!isApiCalled) {
            val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
            val call = serviceGenerator.getPosts()
            call.enqueue(object : Callback<List<PostEntity>> {
                override fun onResponse(call: Call<List<PostEntity>>, response: Response<List<PostEntity>>) {
                    if (response.isSuccessful) {
                        val posts = response.body()
                        posts?.let { posts ->
                            GlobalScope.launch {
                                for (post in posts) {
                                    postDao.insertPost(post)
                                }
                            }
                            postAdapter = PostAdapter(posts.toMutableList())
                            recyclerView.adapter = postAdapter
                        }
                    }
                }

                override fun onFailure(call: Call<List<PostEntity>>, t: Throwable) {
                    // Handle failure
                }
            })
            isApiCalled = true
        }

        postDao.getAllPosts().observe(this, Observer<List<PostEntity>> { posts ->
            if (::postAdapter.isInitialized) {
                postAdapter.updateData(posts.toMutableList())
            } else {
                postAdapter = PostAdapter(posts.toMutableList())
                recyclerView.adapter = postAdapter
            }
        })

        val btnOpenDialog: Button = findViewById(R.id.btDialog)
        btnOpenDialog.setOnClickListener {
            openDialog(
                layoutInflater,
                this,
                this
            )
        }
    }

    override fun onMessageInserted(postEntity: PostEntity) {
        runOnUiThread {
            postAdapter.addMessage(postEntity)
        }
    }

}


