package com.example.myapplication.ui.Tab2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Tab2ViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Fragment 2"
    }
    val text: LiveData<String> = _text
}