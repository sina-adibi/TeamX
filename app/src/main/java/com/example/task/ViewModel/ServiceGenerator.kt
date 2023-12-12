package com.example.task.ViewModel

import com.example.task.Model.PostEntity
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object ServiceGenerator {
    private val client = OkHttpClient.Builder().build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.transport-x.ir/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}

interface ApiService {
    @GET("/api/general/v1/echo")
    fun getPosts(): Call<List<PostEntity>>
}

