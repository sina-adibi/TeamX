package com.example.task.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeVM : ViewModel() {
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    fun setEmail(email: String?) {
        _email.value = email
    }
}