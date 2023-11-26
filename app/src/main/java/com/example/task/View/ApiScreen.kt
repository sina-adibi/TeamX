package com.example.task.View

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.ViewModel.ApiService
import com.example.task.ViewModel.PostAdapter
import com.example.task.ViewModel.ServiceGenerator
import com.example.task.ViewModel.MessageInsertCallback
import com.example.task.Model.PostDao
import com.example.task.Model.PostDatabase
import com.example.task.Model.PostEntity
import com.example.task.utils.openDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiScreenFragment : Fragment(), MessageInsertCallback {

    private lateinit var postAdapter: PostAdapter
    private lateinit var viewModel: ApiVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_api_screen, container, false)
        viewModel = ViewModelProvider(this).get(ApiVM::class.java)

        val recyclerView: RecyclerView = view.findViewById(R.id.myRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        postAdapter = PostAdapter(mutableListOf())
        recyclerView.adapter = postAdapter

        val btnOpenDialog: Button = view.findViewById(R.id.btDialog)
        btnOpenDialog.setOnClickListener {
            openDialog(layoutInflater, requireActivity(), this)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isNetworkAvailable()) {
            viewModel.posts.observe(viewLifecycleOwner) { posts ->
                posts?.let {
                    postAdapter.updateData(it)
                }
            }

            if (!isApiFetched()) {
                viewModel.fetchPostsFromApi()
                setApiFetched()
            } else {
                viewModel.loadPostsFromDatabase()
            }
        } else {
            showNoInternetDialog()
        }
    }

    private fun showNoInternetDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("No Internet")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("OK") { dialog, _ ->
                if (isNetworkAvailable()) {
                    dialog.dismiss()
                    viewModel.posts.observe(viewLifecycleOwner) { posts ->
                        posts?.let {
                            postAdapter.updateData(it)
                        }
                    }
                    if (!isApiFetched()) {
                        viewModel.fetchPostsFromApi()
                        setApiFetched()
                    } else {
                        viewModel.loadPostsFromDatabase()
                    }
                } else {
                    showNoInternetDialog()
                }
            }
            .setCancelable(false) // Prevent dismissing the dialog by tapping outside of it

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
