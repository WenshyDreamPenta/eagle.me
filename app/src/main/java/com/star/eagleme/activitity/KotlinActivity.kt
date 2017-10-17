package com.star.eagleme.activitity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.star.eagleme.R

class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("kotlin", "destroy")
    }

    override fun onResume() {
        super.onResume()
        Log.d("kotlin", myName("OK"))
    }

    fun myName(name: String) : String{
        return "hello,${name}"
    }
}
