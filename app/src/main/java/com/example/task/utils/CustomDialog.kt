package com.example.task.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.databinding.FragmentCustomDialogBinding

class CustomDialog : DialogFragment() {
    private lateinit var binding: FragmentCustomDialogBinding
    private lateinit var dataList: List<String>
    private lateinit var addressEditText1: EditText
    private lateinit var addressEditText2: EditText
    private var isAddress1: Boolean = false // Define isAddress1 as a member variable

    companion object {
        fun newInstance(
            data: List<String>,
            editText1: EditText,
            editText2: EditText,
            isAddress1: Boolean
        ): CustomDialog {
            val dialog = CustomDialog()
            dialog.dataList = data
            dialog.addressEditText1 = editText1
            dialog.addressEditText2 = editText2
            dialog.isAddress1 = isAddress1
            return dialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawableResource(R.drawable.round_corner)

        val recyclerView: RecyclerView = binding.RecyclerNum
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val adapter = CustomAdapter(dataList) { selectedItem ->
            val editText = if (isAddress1) {
                addressEditText1
            } else {
                addressEditText2
            }
            editText.setText(selectedItem)
            dismiss()
        }

        recyclerView.adapter = adapter

        binding.BtnClose.setOnClickListener {
            dismiss()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}

class CustomAdapter(
    private val items: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(android.R.id.text1)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(items[position])
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }
}