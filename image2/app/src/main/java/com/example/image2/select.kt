package com.example.image2

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Gallery
import androidx.appcompat.app.AppCompatActivity


class select : AppCompatActivity() {
    private lateinit var btngallery:Button
    private lateinit var btncamera:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)
        btngallery = findViewById<Button>(R.id.button2)
        btncamera = findViewById<Button>(R.id.button3)

        btncamera.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        btngallery.setOnClickListener {
            val intent = Intent(this, gallery::class.java)
            startActivity(intent)
        }
    }
}
