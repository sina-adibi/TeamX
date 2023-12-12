package com.example.task.ViewModel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task.Model.PostDao
import com.example.task.Model.PostDatabase
import com.example.task.Model.PostEntity
import com.example.task.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostAdapter(
    private val postEntityModel: MutableList<PostEntity>,
    private val postDao: PostDao
) : RecyclerView.Adapter<PostViewHolder>() {

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

//        holder.bindView(context, postEntity, postDao, this,postEntityModel) {
//            CoroutineScope(Dispatchers.IO).launch {
//                postDao.deletePost(postEntity.id)
//            }
//            postEntityModel.remove(postEntity)
//            notifyDataSetChanged()
//        }

        holder.bindView(context, postEntity, postDao, this, postEntityModel) {
            CoroutineScope(Dispatchers.IO).launch {
                postDao.softDeletePost(postEntity.id)
            }
            postEntityModel.remove(postEntity)
            notifyItemRemoved(position)
        }
    }
    fun updateData(newData: List<PostEntity>) {
        postEntityModel.clear()
        postEntityModel.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.fragment_api_screen

    }

    fun addMessage(postEntity: PostEntity) {
        postEntityModel.add(postEntity)
        notifyItemInserted(postEntityModel.size - 1)
    }


    fun deletePost(postEntity: PostEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            postDao.softDeletePost(postEntity.id)
        }
        val position = postEntityModel.indexOf(postEntity)
        if (position != -1) {
            postEntityModel.removeAt(position)
            notifyItemRemoved(position)
        }
    }

}