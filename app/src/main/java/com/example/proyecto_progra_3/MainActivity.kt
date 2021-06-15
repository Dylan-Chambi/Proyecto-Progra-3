package com.example.proyecto_progra_3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginButton = findViewById<Button>(R.id.IniciarSesion)
        val botonRegistrar = findViewById<Button>(R.id.buttonRegistrarse)

        loginButton.setOnClickListener{
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }
        botonRegistrar.setOnClickListener {
            val intent = Intent(this, PantallaRegistrar::class.java)
           startActivity(intent)
        }
    }
}