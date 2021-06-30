package com.example.proyecto_progra_3

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.proyecto_progra_3.databinding.ActivityLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginScreen : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        theme.applyStyle(R.style.AppTheme, true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        
        binding.botonEntrar.setOnClickListener {
            binding.progressBarLogin.visibility = View.VISIBLE
            val email = binding.nombreDeUsuario.text.toString()
            val password = binding.contrasenia.text.toString()
            when{
                email.isEmpty() || password.isEmpty() -> {
                    showShortMessage(this, "La autenticación falló!!")
                    val intent = Intent(this, LoginScreen::class.java)
                    startActivity(intent)
                    finish()
                } else -> {
                signIn(email, password)
                }
            }
        }
    }


    private fun signIn(userEmail: String, userPassword: String){
        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    showShortMessage(baseContext, "Inicio Sesion correctamente!")
                    val intent = Intent(this, MenuActivity::class.java)
                    finishAffinity()
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    showShortMessage(baseContext, "La autenticación falló!!")
                    val intent = Intent(this, LoginScreen::class.java)
                    startActivity(intent)
                    finish()
                }
            }
    }

}