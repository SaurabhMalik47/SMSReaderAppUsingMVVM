package com.example.messagereaderapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var viewModel: MessageViewModel
    private lateinit var nothingToDisplay: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        nothingToDisplay = findViewById(R.id.nothing_to_display)

        if (!isPermissionGranted())
            showPermissionDialog()
        else {
            setupRecyclerView()
        }
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter()
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val divider = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(divider)

        setMessages()
    }

    // Initialise the viewModel Class and then attach an observer to our LiveData
    private fun setMessages() {
        viewModel = ViewModelProvider(this).get(MessageViewModel::class.java)
        viewModel.messages.observe(this, Observer { messages ->
            messages.let {
                if (it.isNotEmpty()) nothingToDisplay.visibility = View.GONE
                else nothingToDisplay.visibility = View.VISIBLE
                messageAdapter.setMessageList(it)
            }
        })
    }

    private fun isPermissionGranted(): Boolean = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_SMS
    ) == PackageManager.PERMISSION_GRANTED

    private fun showPermissionDialog() {
        val alertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(R.string.permission_required_header)
                setMessage(R.string.permission_required_desc_1)
                setPositiveButton(R.string.ok) { _, _ -> initiatePermission() }
                setOnDismissListener { }
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun permissionDeniedDialog() {
        val alertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(R.string.permission_required_header)
                setMessage(R.string.permission_required_desc_2)
                setPositiveButton(R.string.ok) { _, _ -> }
                setOnDismissListener { }
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun initiatePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_SMS),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    setupRecyclerView()
                else permissionDeniedDialog()
            }
            else -> permissionDeniedDialog()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 101
    }
}