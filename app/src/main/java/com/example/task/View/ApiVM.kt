package com.example.task.View


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.ViewModel.ApiService
import com.example.task.ViewModel.ServiceGenerator
import com.example.task.Model.PostDao
import com.example.task.Model.PostDatabase
import com.example.task.Model.PostEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiVM(application: Application) : AndroidViewModel(application) {
    private val postDao: PostDao = PostDatabase.getInstance(application).postDao()

    private val _posts: MutableLiveData<List<PostEntity>> = MutableLiveData()
    val posts: LiveData<List<PostEntity>> = _posts

    init {
        loadPostsFromDatabase()
    }

    fun loadPostsFromDatabase() {
        postDao.getAllPosts().observeForever { posts -> _posts.value = posts
        }
    }


    fun fetchPostsFromApi() {
        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.getPosts()
        call.enqueue(object : Callback<List<PostEntity>> {
            override fun onResponse(
                call: Call<List<PostEntity>>,
                response: Response<List<PostEntity>>
            ) {
                if (response.isSuccessful) {
                    val posts = response.body()
                    posts?.let { fetchedPosts ->
                        viewModelScope.launch {
                            for (post in fetchedPosts) {
                                postDao.insertPost(post)
                            }
                            loadPostsFromDatabase()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<PostEntity>>, t: Throwable) {
                // Handle failure
            }
        })
    }
}

