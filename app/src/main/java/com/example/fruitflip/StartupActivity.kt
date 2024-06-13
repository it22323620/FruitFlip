package com.example.fruitflip

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class StartupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)
        var startbuttn = findViewById<ImageView>(R.id.playBtn)
        startbuttn.setOnClickListener {
            startActivity(Intent(this@StartupActivity, MainActivity::class.java))
        }
    }
}