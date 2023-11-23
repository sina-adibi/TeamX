package com.example.task.View

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.ViewModel.ApiService
import com.example.task.ViewModel.PostAdapter
import com.example.task.ViewModel.ServiceGenerator
import com.example.task.ViewModel.MessageInsertCallback
import com.example.task.Model.PostDao
import com.example.task.Model.PostDatabase
import com.example.task.Model.PostEntity
import com.example.task.utils.openDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiScreenFragment : Fragment(), MessageInsertCallback {

    private lateinit var postAdapter: PostAdapter
    private lateinit var postDao: PostDao
    private lateinit var sharedPrefs: SharedPreferences

    private var isApiCalled = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_api_screen, container, false)

        postDao = PostDatabase.getInstance(requireContext()).postDao()

        val recyclerView: RecyclerView = view.findViewById(R.id.myRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        postAdapter = PostAdapter(mutableListOf())
        recyclerView.adapter = postAdapter

        sharedPrefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        isApiCalled = sharedPrefs.getBoolean("isApiCalled", false)

        if (isApiCalled) {
            loadPostsFromDatabase()
        } else {
            val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
            val call = serviceGenerator.getPosts()
            call.enqueue(object : Callback<List<PostEntity>> {
                override fun onResponse(
                    call: Call<List<PostEntity>>,
                    response: Response<List<PostEntity>>
                ) {
                    if (response.isSuccessful) {
                        val posts = response.body()
                        posts?.let { posts ->
                            GlobalScope.launch {
                                for (post in posts) {
                                    postDao.insertPost(post)
                                }
                            }
                            val editor = sharedPrefs.edit()
                            editor.putBoolean("isApiCalled", true)
                            editor.apply()
                            loadPostsFromDatabase()
                        }
                    }
                }

                override fun onFailure(call: Call<List<PostEntity>>, t: Throwable) {
                    // Handle failure
                }
            })
        }

        val btnOpenDialog: Button = view.findViewById(R.id.btDialog)
        btnOpenDialog.setOnClickListener {
            openDialog(layoutInflater, requireActivity(), this)
        }

        return view
    }

    override fun onMessageInserted(postEntity: PostEntity) {
        postAdapter.addMessage(postEntity)
    }

    private fun loadPostsFromDatabase() {
        postDao.getAllPosts().observe(viewLifecycleOwner, Observer { posts ->
            postAdapter.updateData(posts)
        })
    }

    override fun onDestroyView() {
        sharedPrefs.edit().clear().apply()
        super.onDestroyView()
    }
}

//class ApiScreenFragment : Fragment() ,MessageInsertCallback{
//    private lateinit var postViewModel: PostViewModel
//    private lateinit var postDao: PostDao
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var postAdapter: PostAdapter
//    private lateinit var sharedPrefs: SharedPreferences
//
//    private var isApiCalled = false
//
//    @OptIn(DelicateCoroutinesApi::class)
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_api_screen, container, false)
//
//        postDao = PostDatabase.getInstance(requireContext()).postDao()
//        postViewModel = ViewModelProvider(requireActivity()).get(PostViewModel::class.java)
//
//        recyclerView = view.findViewById(R.id.myRecyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//        postAdapter = PostAdapter(mutableListOf())
//        recyclerView.adapter = postAdapter
//
//        sharedPrefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//        isApiCalled = sharedPrefs.getBoolean("isApiCalled", false)
//
//        if (isApiCalled) {
//            loadPostsFromDatabase()
//        } else {
//            val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
//            val call = serviceGenerator.getPosts()
//            call.enqueue(object : Callback<List<PostEntity>> {
//                override fun onResponse(
//                    call: Call<List<PostEntity>>,
//                    response: Response<List<PostEntity>>
//                ) {
//                    if (response.isSuccessful) {
//                        val posts = response.body()
//                        posts?.let { posts ->
//                            GlobalScope.launch {
//                                for (post in posts) {
//                                    postDao.insertPost(post)
//                                }
//                            }
//                            val editor = sharedPrefs.edit()
//                            editor.putBoolean("isApiCalled", true)
//                            editor.apply()
//                            loadPostsFromDatabase()
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<List<PostEntity>>, t: Throwable) {
//                    // Handle failure
//                }
//            })
//        }
//
//        val btnOpenDialog: Button = view.findViewById(R.id.btDialog)
//        btnOpenDialog.setOnClickListener {
//            openDialog(layoutInflater, requireActivity(), this)
//        }
//
//        return view
//    }
//
//    override fun onMessageInserted(postEntity: PostEntity) {
//        postAdapter.addMessage(postEntity)
//    }
//
//    private fun loadPostsFromDatabase() {
//        postDao.getAllPosts().observe(viewLifecycleOwner, Observer<List<PostEntity>> { posts ->
//            postAdapter.updateData(posts)
//        })
//    }
//
//    override fun onDestroyView() {
//        sharedPrefs.edit().clear().apply()
//        super.onDestroyView()
//    }
//}