package com.example.messagereaderapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel

// This ViewModel class is created to manage the LiveData according to lifecycle of the UI controller (MainActivity)
class MessageViewModel(application: Application) : AndroidViewModel(application) {

    // The 'messages' variable holds MessageLiveData object to which we pass the application context
    val messages = MessageLiveData(application.applicationContext)

}