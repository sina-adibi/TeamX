package com.example.task.utils

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.task.R
import com.example.task.ViewModel.MessageInsertCallback
import com.example.task.Model.PostDao
import com.example.task.Model.PostDatabase
import com.example.task.Model.PostEntity
import com.example.task.ViewModel.PostViewModel
import com.example.task.databinding.DialogBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(DelicateCoroutinesApi::class)
fun openDialog(
    layoutInflater: LayoutInflater,
    activity: Activity,
    messageInsertCallback: MessageInsertCallback
) {
    val dialogBuilder = AlertDialog.Builder(activity)
    val dialogView: View = layoutInflater.inflate(R.layout.dialog, null)
    dialogBuilder.setView(dialogView)
    val alertDialog: AlertDialog = dialogBuilder.create()
    alertDialog.show()

    val binding = DialogBinding.bind(dialogView)
    val etDialogInput = binding.etDialogInput

    val spinnerDialog = binding.spinnerDialog
    val spinnerAdapter = ArrayAdapter.createFromResource(activity, R.array.spinner_city, android.R.layout.simple_spinner_item)
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinnerDialog.adapter = spinnerAdapter

    val checkboxDialog = binding.checkboxDialog
    val checkboxDialog2 = binding.checkboxDialog2
    val checkboxDialog3 = binding.checkboxDialog3
    val checkboxDialog4 = binding.checkboxDialog4
    val checkboxDialog5 = binding.checkboxDialog5

    val postDao: PostDao = PostDatabase.getInstance(activity).postDao()
    ViewModelProvider(activity as ViewModelStoreOwner)[PostViewModel::class.java]

    binding.BtOk.setOnClickListener {
        val inputText = etDialogInput.text.toString()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val DateText = dateFormat.format(Date())
        val SeenText = "1"

        val isChecked = checkboxDialog.isChecked
        val isChecked2 = checkboxDialog2.isChecked
        val isChecked3 = checkboxDialog3.isChecked
        val isChecked4 = checkboxDialog4.isChecked
        val isChecked5 = checkboxDialog5.isChecked

        lateinit var postEntity: PostEntity

        if (inputText.isNotEmpty()) {
            val selectedOption = spinnerDialog.selectedItem.toString()
            postEntity = PostEntity(
                0,
                DateText,
                inputText,
                SeenText,
                selectedOption,
                isChecked,
                isChecked2,
                isChecked3,
                isChecked4,
                isChecked5,
            )
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
    binding.BtCancel.setOnClickListener {
        alertDialog.dismiss()
    }
}
