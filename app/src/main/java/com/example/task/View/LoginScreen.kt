package com.example.task.View

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.task.ViewModel.LoginVM
import com.example.task.databinding.FragmentLoginScreenBinding

class LoginScreenFragment : Fragment() {
    private lateinit var binding: FragmentLoginScreenBinding
    private lateinit var loginViewModel: LoginVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        val view = binding.root

        loginViewModel = ViewModelProvider(this).get(LoginVM::class.java)

        if (loginViewModel.hasSavedCredentials(requireContext())) {
            navigateToApiScreen()
        }

        binding.idBtnLogin.setOnClickListener {
            val email = binding.idEdtEmail.text.toString()
            val password = binding.idEdtPassword.text.toString()

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
        val action: NavDirections = LoginScreenFragmentDirections.actionLoginScreenFragmentToViewPager2()
        findNavController().navigate(action)
    }
}
