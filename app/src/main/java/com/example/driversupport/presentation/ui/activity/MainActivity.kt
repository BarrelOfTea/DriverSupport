package com.example.driversupport.presentation.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.driversupport.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        setTheme(R.style.Theme_DriverSupport)
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity)
    }

}