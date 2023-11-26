package com.example.task.View

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import com.example.task.R
import androidx.navigation.fragment.findNavController

class LoginScreenFragment : Fragment() {
    private lateinit var loginViewModel: LoginVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_screen, container, false)

        val emailEdt = view.findViewById<EditText>(R.id.idEdtEmail)
        val passwordEdt = view.findViewById<EditText>(R.id.idEdtPassword)
        val loginBtn = view.findViewById<Button>(R.id.idBtnLogin)

        loginViewModel = ViewModelProvider(this).get(LoginVM::class.java)

        if (loginViewModel.hasSavedCredentials(requireContext())) {
            navigateToApiScreen()
        }

        loginBtn.setOnClickListener {
            val email = emailEdt.text.toString()
            val password = passwordEdt.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(
                    requireContext(),
                    "Please Enter Email and Password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                loginViewModel.saveCredentials(requireContext(), email, password)
                navigateToApiScreen()
            }
        }

        return view
    }

    private fun navigateToApiScreen() {
        val action: NavDirections = LoginScreenFragmentDirections.actionLoginScreenFragmentToApiScreenFragment()
        findNavController().navigate(action)
    }
}