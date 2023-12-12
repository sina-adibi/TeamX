package com.example.task.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task.Model.PostDao
import com.example.task.Model.PostDatabase
import com.example.task.ViewModel.PostAdapter
import com.example.task.databinding.FragmentDelApiScreenBinding


class DelApiScreen : Fragment() {
    private lateinit var binding: FragmentDelApiScreenBinding
    private lateinit var postAdapter: PostAdapter
    private lateinit var postDao: PostDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDelApiScreenBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.RecyclerViewDelete
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        postDao = PostDatabase.getInstance(requireContext()).postDao()
        postAdapter = PostAdapter(mutableListOf(), postDao)

        recyclerView.adapter = postAdapter

        // Observe deleted posts and update the RecyclerView
        postDao.getDeletedPosts().observe(viewLifecycleOwner) { deletedPosts ->
            postAdapter.updateData(deletedPosts)
        }
    }
}
