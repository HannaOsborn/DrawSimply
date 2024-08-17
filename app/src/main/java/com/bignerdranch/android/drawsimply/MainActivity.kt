package com.bignerdranch.android.drawsimply

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var newButton: ImageButton
    private lateinit var androidView: ImageView

    @SuppressLint("SetTextI18n")
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        newButton = findViewById(R.id.new_button)
        androidView = findViewById(R.id.androidView)

        newButton.setOnClickListener {
            Toast.makeText(this, R.string.new_canvas_toast, Toast.LENGTH_SHORT).also {
                it.setGravity(
                    Gravity.TOP, 0, 0
                )
            }.show()
            val intent = Intent(this, DrawActivity::class.java)
            startActivity(intent)
        }
    }
}
