package com.example.messagereaderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var messageList = emptyList<Message>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val number = view.findViewById<TextView>(R.id.number)
        private val details = view.findViewById<TextView>(R.id.details)

        fun bind(message: Message) {
            number.text = message.phoneNumber
            details.text = message.messageDetails
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_of_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = messageList.size

    fun setMessageList(newMessageList: List<Message>) {
        this.messageList = newMessageList
        notifyDataSetChanged()
    }
}