package com.example.task.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.task.MainActivity
import com.example.task.R


class HomeScreen : Fragment() {

    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
        const val PASSWORD_KEY = "password_key"
    }

    private lateinit var sharedpreferences: SharedPreferences
    private var email: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home_screen, container, false)

        sharedpreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        email = sharedpreferences.getString(EMAIL_KEY, null)

        val welcomeTV = view.findViewById<TextView>(R.id.idTVWelcome)
        welcomeTV.text = "Welcome $email"

        val logoutBtn = view.findViewById<Button>(R.id.idBtnLogout)
        logoutBtn.setOnClickListener {
            val editor = sharedpreferences.edit()
            editor.clear()
            editor.apply()
            val i = Intent(requireActivity(), MainActivity::class.java)
            startActivity(i)
            requireActivity().finish()
        }

        val apiBtn = view.findViewById<Button>(R.id.ApiBtn)
        apiBtn.setOnClickListener {
            val action: NavDirections = HomeScreenDirections.actionHomeScreenFragmentToApiScreenFragment()
            findNavController().navigate(action)
        }

        return view
    }
}