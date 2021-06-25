package com.example.proyecto_progra_3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.android.gms.maps.CameraUpdateFactory

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler(Looper.getMainLooper()).postDelayed(

            {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000
        )
    }
}