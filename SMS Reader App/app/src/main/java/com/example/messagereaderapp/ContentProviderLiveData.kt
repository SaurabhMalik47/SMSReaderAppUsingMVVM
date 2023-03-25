package com.example.messagereaderapp

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import androidx.lifecycle.MutableLiveData

// Here, we are creating our own implementation of MutableLiveData class
// For that, we will create an ContentObserver inside, which will notify the MutableLiveData class,
// whenever there is any change in the URI and post the new values.
// Further, this MutableLiveData will post the new values to the UI controller (MainActivity)
// So, create a new abstract class ContentProviderLiveData
// This will act as base class for our implementation of MutableLiveData class
abstract class ContentProviderLiveData<T>(
    private val context: Context, // It will be used to resolve contentResolver query
    private val uri: Uri // The ContentObserver will observe the URI
) : MutableLiveData<T>() {
    // <T> Generic parameter type, which will be used to pass the type of object we want the LiveData to wrap upon
    // In our case, it will be of 'Message' type

    // ContentObserver is a class which receives a callback for changes to content
    private lateinit var observer: ContentObserver

    // It is called when an observer is attached with LiveData
    override fun onActive() {
        // Post the initial value of ContentProvider to the observer of our LiveData
        postValue(getContentProviderValue()) // It takes a value as a parameter, and passes it to main thread to set the value

        // After setting initial value, Create the ContentObserver object
        observer = object : ContentObserver(null) {
            // onChange() method is called whenever there is any change in the value of the ContentProvider it is observing
            // In our case, it will be the context
            override fun onChange(selfChange: Boolean) {
                postValue(getContentProviderValue()) // Set the latest value on the UI controller
            }
        }

        // Register our ContentObserver object with the uri of the ContentProvider we want to observe
        context.contentResolver.registerContentObserver(uri, true, observer)
    }

    // It is called when observer is removed or destroyed
    override fun onInactive() {
        // Unregister the ContentObserver
        context.contentResolver.unregisterContentObserver(observer)
    }

    // Below abstract function is implemented to get latest values from ContentProvider
    abstract fun getContentProviderValue(): T
}