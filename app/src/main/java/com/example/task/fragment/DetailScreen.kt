package com.example.task.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.task.R


class DetailScreen : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_screen, container, false)

        val message = arguments?.getString("post_message")
        val tvmassage: TextView = view.findViewById(R.id.tvmassage)
        tvmassage.text = message

        return view
    }
}
