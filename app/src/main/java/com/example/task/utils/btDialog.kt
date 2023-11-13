package com.example.task.utils

import android.app.Activity
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.api.ApiService
import com.example.task.api.PostAdapter
import com.example.task.api.ServiceGenerator
import com.example.task.room.PostDao
import com.example.task.room.PostDatabase
import com.example.task.room.PostEntity
import com.example.task.room.PostViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(DelicateCoroutinesApi::class)
fun openDialog(
    layoutInflater: LayoutInflater,
    activity: Activity,
    messageInsertCallback: MessageInsertCallback

) {

    val dialogBuilder = android.app.AlertDialog.Builder(activity)
    dialogBuilder.setTitle("Insert Message")
    dialogBuilder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
        dialog.dismiss()
    }
    val dialogView: View = layoutInflater.inflate(R.layout.dialog, null)
    dialogBuilder.setView(dialogView)
    val alertDialog: android.app.AlertDialog = dialogBuilder.create()
    alertDialog.show()

    val etDialogInput: EditText = dialogView.findViewById(R.id.etDialogInput)

    val postDao: PostDao = PostDatabase.getInstance(activity).postDao()
    ViewModelProvider(activity as ViewModelStoreOwner)[PostViewModel::class.java]

    alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener {
        val inputText = etDialogInput.text.toString()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val DateText = dateFormat.format(Date())
        val SeenText = "1"
        lateinit var postEntity: PostEntity

        if (inputText.isNotEmpty() || DateText.isNotEmpty() || SeenText.isNotEmpty()) {
            postEntity = PostEntity(0, DateText, inputText, SeenText)
            GlobalScope.launch {
                postDao.insertMessage(postEntity)
                withContext(Dispatchers.Main) {
                    messageInsertCallback.onMessageInserted(postEntity)
                }
            }
            alertDialog.dismiss()
        } else {
            Snackbar.make(it, "Message cannot be Empty", Snackbar.LENGTH_LONG).show()
        }
    }
}