package com.example.task.View

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task.Model.PostDao
import com.example.task.Model.PostDatabase
import com.example.task.ViewModel.PostAdapter
import com.example.task.ViewModel.MessageInsertCallback
import com.example.task.Model.PostEntity
import com.example.task.ViewModel.ApiVM
import com.example.task.databinding.FragmentApiScreenBinding
import com.example.task.utils.openDialog


class ApiScreenFragment : Fragment(), MessageInsertCallback {

    private lateinit var postAdapter: PostAdapter
    private lateinit var viewModel: ApiVM
    private lateinit var binding: FragmentApiScreenBinding
    private lateinit var postDao: PostDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApiScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(ApiVM::class.java)

        val recyclerView: RecyclerView = binding.myRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

//        postAdapter = PostAdapter(mutableListOf())
        postDao = PostDatabase.getInstance(requireContext()).postDao()
        postAdapter = PostAdapter(mutableListOf(), postDao)
        recyclerView.adapter = postAdapter

        binding.btDialog.setOnClickListener {
            openDialog(layoutInflater, requireActivity(), this)
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            posts?.let {
                postAdapter.updateData(it)
            }
        }
        if (isNetworkAvailable() && !isApiFetched()) {
            if (!isApiFetched()) {
                viewModel.fetchPostsFromApi()
                setApiFetched()
            }
        } else if (!isNetworkAvailable() && !isApiFetched()) {
            showNoInternetDialog()
        } else if (isApiFetched()) {
            viewModel.loadPostsFromDatabase()
        }
    }

    private fun showNoInternetDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("No Internet")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("OK") { dialog, _ ->
                viewModel.posts.observe(viewLifecycleOwner) { posts ->
                    posts?.let {
                        postAdapter.updateData(it)
                    }
                }
                if (isNetworkAvailable() && !isApiFetched()) {
                    if (!isApiFetched()) {
                        viewModel.fetchPostsFromApi()
                        setApiFetched()
                    }
                } else if (!isNetworkAvailable() && !isApiFetched()) {
                    showNoInternetDialog()
                } else if (isApiFetched()) {
                    viewModel.loadPostsFromDatabase()
                }
            }
            .setCancelable(false)

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun isApiFetched(): Boolean {
        val sharedPrefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("isApiCalled", false)
    }

    private fun setApiFetched() {
        val sharedPrefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("isApiCalled", true)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.posts.removeObservers(viewLifecycleOwner)
    }

    override fun onMessageInserted(postEntity: PostEntity) {
        postAdapter.addMessage(postEntity)
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}
