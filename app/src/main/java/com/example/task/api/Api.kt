package com.example.task.api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


data class PostModel(
    val id: Int? = null,
    val saveDate: String? = null,
    val message: String? = null,
    val seen: String? = null
)

object ServiceGenerator {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.transport-x.ir")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}


interface ApiService {
    @GET("/api/general/v1/echo")
    fun getPosts(): Call<List<PostModel>>
}

class PostAdapter(val postModel: List<PostModel>) : RecyclerView.Adapter<PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_post, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postModel.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        return holder.bindVeiw(postModel[position])
    }


}


class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    private val tvBody: TextView = itemView.findViewById(R.id.tvBody)
    private val tvSeen: TextView = itemView.findViewById(R.id.tvSeen)

    fun bindVeiw(postModel: PostModel) {
        tvTitle.text = postModel.saveDate
        tvBody.text = postModel.message
        if (postModel.seen == "0") {
            tvSeen.text = "پیام خوانده نشده"
        } else if (postModel.seen == "1") {
            tvSeen.text = "پیام خوانده شده"
        } else {
            tvSeen.text = "error"
        }
    }
}
