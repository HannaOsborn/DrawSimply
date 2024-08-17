package com.bignerdranch.android.drawsimply

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
        }, 2000)

    }
}