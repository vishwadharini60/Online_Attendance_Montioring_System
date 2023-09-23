package com.example.attendance_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MyAdapter(context: Context, records: ArrayList<String>) :
    ArrayAdapter<String>(context, 0, records) {

    // Adding interface to send the clicked button to activity
    interface AdapterCallback {
        fun onButtonClicked(value: String)
    }

    private var callback: AdapterCallback? = null

    fun setAdapterCallback(callback: AdapterCallback) {
        this.callback = callback
    }

    //creating the inflated view
    @SuppressLint("SuspiciousIndentation")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertedView = convertView
        val item = getItem(position)

        if (convertedView == null) {
            convertedView =
                LayoutInflater.from(context).inflate(R.layout.listview, parent, false)
        }

        val listTxt = convertedView!!.findViewById<TextView>(R.id.List_txt)
        val listBut = convertedView.findViewById<Button>(R.id.List_but)

        listTxt.text = item
           listBut.setOnClickListener {
               listBut.isEnabled=false
               val str=listTxt.text.toString()
               callback?.onButtonClicked(str)
           }

        return convertedView
    }



}
