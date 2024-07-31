package com.tunahankaryagdi.firstproject.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tunahankaryagdi.firstproject.BuildConfig
import com.tunahankaryagdi.firstproject.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var firstValue = 1
        var secondValue = 1

        while (firstValue < 100) {
            println(firstValue)
            val temp = firstValue
            firstValue = secondValue
            secondValue += temp
        }

        println("${BuildConfig.API_KEY}")
    }
}
