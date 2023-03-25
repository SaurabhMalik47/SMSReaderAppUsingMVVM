package com.example.messagereaderapp

import android.content.Context
import android.provider.Telephony

// Now, we will implement ContentProviderLiveData class in our MessageLiveData class
// Here, extend ContentProviderLiveData and pass it in a list of messages in the place of generic type <T>
class MessageLiveData(private val context: Context) : ContentProviderLiveData<List<Message>>(context, URI) {

    // companion objects are equivalent to the Static class,
    // so, any variables or methods can be directly accessed from the containing class.
    companion object {
        val URI = Telephony.Sms.Inbox.CONTENT_URI
    }

    // Here, we will use contentResolver to query the list of messages from the contentProvider
    private fun getMessages(context : Context): List<Message> {
        val listOfMessages = mutableListOf<Message>()
        val projection = arrayOf(
            Telephony.Sms.Inbox.ADDRESS,
            Telephony.Sms.Inbox.BODY
        )

        val cursor = context.contentResolver.query(
            URI,
            projection,
            null,
            null,
            Telephony.Sms.Inbox.DEFAULT_SORT_ORDER
        )
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val phoneNumber = cursor.getString(0)
                val messageDetails = cursor.getString(1)

                listOfMessages.add(Message(phoneNumber, messageDetails))
            } while (cursor.moveToNext())

            cursor.close()
        }

        return listOfMessages
    }

    // Override getContentProviderValue() function to make it return the list of messages via getMessages() function
    // Whenever there is any change in data, this function will query and get the new data
    override fun getContentProviderValue() = getMessages(context)
}
// Now, we want to use this MessageLiveData class to show a list of messages in the MainActivity in a recycler view
// So, Create MessageViewModel class
