package com.example.task.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import com.example.task.ViewModel.PostAdapter
import com.example.task.Model.PostDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun btDialogDelete(
    context: Context,
    postId: Int,
    postDao: PostDao,
    postAdapter: PostAdapter,
    updateCallback: () -> Unit
) {
    val positiveButtonClick = DialogInterface.OnClickListener { dialog, which ->
        CoroutineScope(Dispatchers.IO).launch {
            postDao.deletePost(postId)
        }
        updateCallback()
    }

    val negativeButtonClick = DialogInterface.OnClickListener { dialog, which ->
        Toast.makeText(
            context,
            android.R.string.no, Toast.LENGTH_SHORT
        ).show()
    }

    val builder = AlertDialog.Builder(context)
    with(builder) {
        setTitle("Confirmation")
        setMessage("Are you sure you want to delete this post?")
        setPositiveButton("Delete", positiveButtonClick)
        setNegativeButton("Cancel", negativeButtonClick)
        show()
    }
}