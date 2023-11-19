package com.example.task.fragment

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
import androidx.navigation.NavDirections
import com.example.task.R
import androidx.navigation.fragment.findNavController

class LoginScreenFragment : Fragment() {

    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
        const val PASSWORD_KEY = "password_key"
    }

    private lateinit var sharedpreferences: SharedPreferences
    private var email: String? = null
    private var password: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_screen, container, false)

        val emailEdt = view.findViewById<EditText>(R.id.idEdtEmail)
        val passwordEdt = view.findViewById<EditText>(R.id.idEdtPassword)
        val loginBtn = view.findViewById<Button>(R.id.idBtnLogin)

        sharedpreferences =
            requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        email = sharedpreferences.getString(EMAIL_KEY, null)
        password = sharedpreferences.getString(PASSWORD_KEY, null)

        loginBtn.setOnClickListener {
            if (TextUtils.isEmpty(emailEdt.text.toString()) && TextUtils.isEmpty(passwordEdt.text.toString())) {
                Toast.makeText(
                    requireContext(),
                    "Please Enter Email and Password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val editor = sharedpreferences.edit()

                editor.putString(EMAIL_KEY, emailEdt.text.toString())
                editor.putString(PASSWORD_KEY, passwordEdt.text.toString())

                editor.apply()


                val action: NavDirections = LoginScreenFragmentDirections.actionLoginScreenFragmentToApiScreenFragment()
                findNavController().navigate(action)
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        if (email != null && password != null) {
            val action: NavDirections = LoginScreenFragmentDirections.actionLoginScreenFragmentToApiScreenFragment()
            findNavController().navigate(action)
        }
    }
}

//                val i = Intent(requireActivity(), ApiScreen::class.java)
//                startActivity(i)
//                requireActivity().finish()

//            val action = LoginScreenFragmentDirections.actionLoginScreenFragmentToApiScreenFragment()
//            findNavController().navigate(action)