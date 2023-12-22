package com.example.task.ViewModel

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.task.Model.PostDao
import com.example.task.Model.PostDatabase
import com.example.task.Model.PostEntity
import com.example.task.R
import com.example.task.View.ViewPagerDirections
import com.example.task.utils.btDialogDelete
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale



class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    private val tvBody: TextView = itemView.findViewById(R.id.tvBody)
    private val tvSpinner: TextView = itemView.findViewById(R.id.tvSpinner)
    private val tvCheckBox: TextView = itemView.findViewById(R.id.tvCheckBox)
    private val btSeen: Button = itemView.findViewById(R.id.btSeen)
    private val btDetail: Button = itemView.findViewById(R.id.btDetail)
    val btnDeleteDialog: Button = itemView.findViewById(R.id.btDelete)
    fun bindView(
        context: Context,
        postEntity: PostEntity,
        postDao: PostDao,
        postAdapter: PostAdapter,
        newData: List<PostEntity>,
        updateCallback: () -> Unit,
    ) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", Locale.getDefault())

        val date = inputFormat.parse(postEntity.saveDate)
        val formattedDate = outputFormat.format(date)

        tvTitle.text = formattedDate
        tvBody.text = postEntity.message
        btSeen.background = ContextCompat.getDrawable(itemView.context, R.drawable.button_seen)
        tvSpinner.text = postEntity.spinnerSelection

        val checkboxTextList = mutableListOf<String>()

        if (postEntity.checkbox1) {
            checkboxTextList.add("نامشخص")
        }
        if (postEntity.checkbox2) {
            checkboxTextList.add("اقتصادی")
        }
        if (postEntity.checkbox3) {
            checkboxTextList.add("ممتاز")
        }
        if (postEntity.checkbox4) {
            checkboxTextList.add("تاکسی")
        }
        if (postEntity.checkbox5) {
            checkboxTextList.add("نامشخص")
        }
        tvCheckBox.text = checkboxTextList.joinToString(" , ")

        if (postEntity.seen == "0") {
            btSeen.text = "پیام خوانده نشده"
            btSeen.background.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
        } else if (postEntity.seen == "1") {
            btSeen.text = "پیام خوانده شده"
            btSeen.background.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN)
        } else {
            btSeen.text = "error"
        }

        btSeen.setOnClickListener {
            val updatedSeenValue = if (postEntity.seen == "0") "1" else "1"
            postEntity.seen = updatedSeenValue

            CoroutineScope(Dispatchers.IO).launch {
                val dao = PostDatabase.getInstance(itemView.context).postDao()
                dao.updatePostSeen(postEntity.id, updatedSeenValue)
            }

            if (updatedSeenValue == "1") {
                btSeen.text = "پیام خوانده شده"
                btSeen.background.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN)
            } else {
                btSeen.text = "پیام خوانده نشده"
                btSeen.background.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
            }
        }

        btDetail.setOnClickListener {
            val message = postEntity.message
            val action = ViewPagerDirections.actionViewPager2ToDetailScreen(message)
            itemView.findNavController().navigate(action)
        }


        btnDeleteDialog.setOnClickListener {
            try {
                btDialogDelete(context, postEntity.id, postDao, postAdapter) {
                    updateCallback.invoke()
                }
            } catch (e: Exception) {
                Log.e("DeleteDialog", "Error deleting post: ${e.message}")
            }
        }
    }
}
