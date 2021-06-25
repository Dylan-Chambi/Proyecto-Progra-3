package com.example.proyecto_progra_3

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.proyecto_progra_3.databinding.LayoutRegisterScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbFirebase: FirebaseDatabase
    private lateinit var bindingRegister: LayoutRegisterScreenBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register_screen)
        auth = Firebase.auth
        bindingRegister = LayoutRegisterScreenBinding.inflate(layoutInflater)
        setContentView(bindingRegister.root)
        database = Firebase.database.reference

        bindingRegister.registerButton.setOnClickListener  {
            val userName = bindingRegister.textoNombreDeUsuario.text.toString()
            val userPassword = bindingRegister.textoContrasenia.text.toString()
            val userPasswordR = bindingRegister.textoConfirmarContrasenia.text.toString()
            val userPhone = bindingRegister.numeroDeTelefono.text.toString()
            val userEmail = bindingRegister.correoUsuario.text.toString()

            if(userPassword == userPasswordR && userPassword.isNotEmpty() && userEmail.isNotEmpty() && userName.length >= 5 && userPassword.length >= 6){
                register(userEmail, userPassword,userName, userPhone)
            }else{
                when {
                    userPassword.isEmpty() -> showLongMessage(this, "The password cannot be empty.")
                    userPassword != userPasswordR -> showLongMessage(this, "Your passwords do not match.")
                    userPassword.length < 6 -> showLongMessage(this, "Your password must be at least 6 characters.")
                    userEmail.isEmpty() -> showLongMessage(this, "Your email address cannot be empty")
                    userName.length < 5 -> showLongMessage(this, "Your username must be at least 5 characters.")
                    else -> showLongMessage(this, "Unknown error")
                }
            }
        }
    }

    private fun register(userEmail: String, userPassword: String, userName: String, userPhone: String){
        auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                database = FirebaseDatabase.getInstance().getReference("Users")
                val user = User(userName, userEmail, userPhone, userPassword)
                database.child(userName).setValue(user).addOnSuccessListener {
                    showShortMessage(baseContext,"Register success.")
                }.addOnFailureListener{
                    showLongMessage(this, "Your account was created but: "+it.message!!)
                }
                Log.d(TAG, "createUserWithEmail:success")
                val userAuth = auth.currentUser
                val intent = Intent(this, LoginScreen::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                showShortMessage(baseContext, task.exception?.message!!)
            }
        }
    }
}