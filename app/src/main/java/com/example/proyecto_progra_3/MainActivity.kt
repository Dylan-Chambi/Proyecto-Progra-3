package com.example.proyecto_progra_3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var loginButton: Button
    lateinit var registerButton: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        loginButton = findViewById<Button>(R.id.IniciarSesion)
        registerButton = findViewById<Button>(R.id.buttonRegistrarse)

        loginButton.setOnClickListener{
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
           startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if(user != null){
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }
}