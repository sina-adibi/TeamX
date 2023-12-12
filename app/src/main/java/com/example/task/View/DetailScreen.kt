package com.example.task.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.task.R
import com.example.task.databinding.FragmentApiScreenBinding
import com.example.task.databinding.FragmentDetailScreenBinding

class DetailScreen : Fragment() {
    private lateinit var binding: FragmentDetailScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailScreenBinding.inflate(inflater, container, false)
        val view = binding.root

        val message = arguments?.getString("message")
        binding.tvmassage.text = message

        return view
    }
}
