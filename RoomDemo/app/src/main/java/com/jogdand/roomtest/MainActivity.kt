package com.jogdand.roomtest

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class MainActivity : AppCompatActivity() {

    private var vm: MyVm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<ImageButton>(R.id.btn).setOnClickListener {
            val newData = Data()
            val editText = findViewById<EditText>(R.id.et)
            newData.value = editText.text.toString()
            newData.dateTime = Calendar.getInstance().timeInMillis.toString()
            vm?.addData(newData)
            editText.text = null
        }
        vm = ViewModelProviders.of(this).get(MyVm::class.java)
        vm?.dataList?.observe(this, Observer<MutableList<Data>> {
            if (it == null) {
                return@Observer
            }
            findViewById<RecyclerView>(R.id.rv).adapter = DataAdapter(it)
        })
    }
}
