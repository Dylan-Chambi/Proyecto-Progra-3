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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        /**
        val usuario1 = Usuario("Fer123", "Fernando Alvarado Lanza", 77716326, "Pass123")
        val usuario2 = Usuario("Salao", "Samuel Escobar Bejarano", 77585417, "pbbs159")
        val usuario3 = Usuario("Pary", "Luis Andres Paricollo Parra", 77585417, "samusalao")
        val usuario4 = Usuario("", "Dylan Chambi Frontanilla", 0, "")
        */
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        
        binding.botonEntrar.setOnClickListener {
            binding.progressBarLogin.visibility = View.VISIBLE
            val email = binding.nombreDeUsuario.text.toString()
            val password = binding.contrasenia.text.toString()
            when{
                email.isEmpty() || password.isEmpty() -> {
                    showShortMessage(this, "Authentication failed.")
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
                    showShortMessage(baseContext, "Authentication success.")
                    val intent = Intent(this, MenuActivity::class.java)
                    finishAffinity()
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    showShortMessage(baseContext, "Authentication failed.")
                    val intent = Intent(this, LoginScreen::class.java)
                    startActivity(intent)
                    finish()
                }
            }
    }

}