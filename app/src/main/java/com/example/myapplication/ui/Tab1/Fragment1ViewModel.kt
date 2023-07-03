package com.example.myapplication.ui.Tab1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Fragment1ViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Fragment 1"
    }
    val text: LiveData<String> = _text
}