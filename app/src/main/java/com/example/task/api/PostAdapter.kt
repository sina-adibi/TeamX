package com.example.task.api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R

class PostAdapter(val postModel: MutableList<PostModel>):RecyclerView.Adapter<PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.card_post,parent,false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postModel.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        return holder.bindVeiw(postModel[position])
    }


}

class PostViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
    private val tvTitle :TextView=itemView.findViewById(R.id.tvTitle)
    private val tvBody :TextView=itemView.findViewById(R.id.tvBody)
    fun bindVeiw(postModel: PostModel){
        tvTitle.text=postModel.title
        tvBody.text=postModel.body
    }
}