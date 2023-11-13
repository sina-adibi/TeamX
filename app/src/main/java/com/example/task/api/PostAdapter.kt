package com.example.task.api

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.room.PostDatabase
import com.example.task.room.PostEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
class PostAdapter(private val postEntityModel: MutableList<PostEntity>) :
    RecyclerView.Adapter<PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_post, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (postEntityModel.isEmpty()) {
            0
        } else {
            postEntityModel.size
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val context = holder.itemView.context
        val postDao = PostDatabase.getInstance(context).postDao()
        val postEntity = postEntityModel[position]

        holder.bindView(context, postEntity, postDao, this,postEntityModel) {
            CoroutineScope(Dispatchers.IO).launch {
                postDao.deletePost(postEntity.id)
            }
            postEntityModel.remove(postEntity)
            notifyDataSetChanged()
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (postEntityModel.isEmpty()) {
            R.layout.activity_api_screen // Use your empty view layout here
        } else {
            R.layout.card_post // Use your post item layout here
        }
    }
}