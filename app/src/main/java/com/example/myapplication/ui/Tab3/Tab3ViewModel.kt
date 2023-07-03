package com.example.myapplication.ui.Tab3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Tab3ViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Tab the egg to\nhatch a dinosaur!!"
    }
    val text: LiveData<String> = _text
}