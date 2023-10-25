package com.example.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.task.api.ApiService
import com.example.task.api.PostModel
import com.example.task.api.ServiceGenerator
import okhttp3.Response
import retrofit2.Call
import javax.security.auth.callback.Callback

class api_screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_screen)

        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.getPosts()

        val button = findViewById<Button>(R.id.btnClick)
        button.setOnClickListener {
            call.enqueue(object : retrofit2.Callback<MutableList<PostModel>> {
                override fun onResponse(
                    call: Call<MutableList<PostModel>>,
                    response: retrofit2.Response<MutableList<PostModel>>
                ) {
                    if (response.isSuccessful){
                        Log.e("Success",response.body().toString())
                    }                }

                override fun onFailure(call: Call<MutableList<PostModel>>, t: Throwable) {
t.printStackTrace()
                    Log.e("error",t.message.toString())

                }
            })
        }

    }
}

