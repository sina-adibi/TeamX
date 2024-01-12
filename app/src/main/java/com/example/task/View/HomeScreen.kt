package com.example.task.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.task.MainActivity
import com.example.task.ViewModel.HomeVM
import com.example.task.databinding.FragmentHomeScreenBinding

class HomeScreen : Fragment() {
    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
    }

    private lateinit var sharedpreferences: SharedPreferences
    private lateinit var viewModel: HomeVM
    private lateinit var binding: FragmentHomeScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        val view = binding.root

        sharedpreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        viewModel = ViewModelProvider(this).get(HomeVM::class.java)
        viewModel.setEmail(sharedpreferences.getString(EMAIL_KEY, null))

        viewModel.email.observe(viewLifecycleOwner) { email ->
            binding.idTVWelcome.text = "Welcome $email"
        }

        binding.idBtnLogout.setOnClickListener {

            val editor = sharedpreferences.edit()
            editor.clear()
            editor.apply()
            val i = Intent(requireActivity(), MainActivity::class.java)
            startActivity(i)
            requireActivity().finish()
        }

        binding.ApiBtn.setOnClickListener {
            val action: NavDirections = HomeScreenDirections.actionHomeScreenFragmentToViewPager22()
            findNavController().navigate(action)
        }

        return view
    }
}
