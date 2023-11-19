package com.example.task.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.api.ApiService
import com.example.task.api.PostAdapter
import com.example.task.api.ServiceGenerator
import com.example.task.room.MessageInsertCallback
import com.example.task.room.PostDao
import com.example.task.room.PostDatabase
import com.example.task.room.PostEntity
import com.example.task.room.PostViewModel
import com.example.task.utils.openDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ApiScreenFragment : Fragment(), MessageInsertCallback {

    private lateinit var postViewModel: PostViewModel
    private lateinit var postDao: PostDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter

    var isApiCalled = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_api_screen, container, false)

        postDao = PostDatabase.getInstance(requireContext()).postDao()
        postViewModel = ViewModelProvider(requireActivity()).get(PostViewModel::class.java)

        recyclerView = view.findViewById(R.id.myRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

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

        postDao.getAllPosts().observe(viewLifecycleOwner, Observer<List<PostEntity>> { posts ->
            recyclerView.adapter = PostAdapter(posts.toMutableList())
        })

        val btnOpenDialog: Button = view.findViewById(R.id.btDialog)
        btnOpenDialog.setOnClickListener {
            openDialog(layoutInflater, requireActivity(), this)
        }
        return view
    }

    override fun onMessageInserted(postEntity: PostEntity) {
        postAdapter.addMessage(postEntity)
    }
}